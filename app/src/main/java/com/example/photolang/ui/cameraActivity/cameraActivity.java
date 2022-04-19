package com.example.photolang.ui.cameraActivity;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;

import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.example.photolang.R;
import com.example.photolang.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/*
public class Card {
    ImageView imageview;
    TextView numberResult;
    TextView translationResult;
}
*/

// Video, live stream of the photo frames, you can set frame rate (how many frames per seconds)
// Feed in the captured frame into the model (not single classification, objects detection)
// YOLO => Classes (texts or the indices) and bounding box cordinates (x, y) on the frame

public class cameraActivity extends AppCompatActivity {
    private static final int DIM_IMG_SIZE_X = 224;
    private static final int DIM_IMG_SIZE_Y = 224;
    int imageSize = 224;
    Button btnTakePic;
    ImageView imageview;
    TextView numberResult;
    TextView translationResult;
    // Collection container
    Interpreter interpreter;

    static final String MODEL_PATH = "model.tflite";
    private final float[][] labelProbArray = new float[1][10];
    final String ASSOCIATED_AXIS_LABELS = "labels.txt";
    List<String> associatedAxisLabels = null;
public String[] labels() throws IOException {
   //allow permission to read from external storage
    BufferedReader br = new BufferedReader (new InputStreamReader(getAssets().open("labels.txt"), "UTF-8"));
    List<String> listOfStrings
            = new ArrayList<String>();
       //BufferedReader bf = new BufferedReader(new FileReader("labels.txt"));
       String line = br.readLine();
       while (line != null) {
           listOfStrings.add(line);
           line = br.readLine();
       }
       br.close();
    // storing the data in arraylist to array
    String[] labels
            = listOfStrings.toArray(new String[0]);
    return labels;
}

    private MappedByteBuffer loadModelFile (Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    /*
    List [(Thumbnail, Text)]

    Button -> oncick:
     fires the classify, Then append the reuslts to the list
     and render the list.

     */

    ActivityResultLauncher<String> mgetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            Log.d("cameraActivity", "onActivityResult: " + result);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
                imageview.setImageBitmap(bitmap);
                bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);
                classify(bitmap);
              //  interpreter = new Interpreter(loadModelFile(cameraActivity.this));
              //  convertBitmapToByteBuffer(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    private void classify(Bitmap bitmap) {
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(224 * 224 * 3 * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            byteBuffer.rewind();
            int [] intValues = new int[224 * 224];
            bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            int pixel = 0;
            for (int i = 0; i < 224; i++) {
                for (int j = 0; j < 224; j++) {
                    int pix = intValues[pixel++];
                    byteBuffer.putFloat(((pix >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((pix >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((pix & 0xFF) * (1.f / 255.f));

                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String[] classes = {"Apple", "Banana", "beetroot",
                    "bell pepper" ,
                    "cabbage" ,
                    "carrot" ,
                    "cauliflower" ,
                    "chili pepper" ,
                    "corn" ,
                    "cucumber"};
    
            translationResult.setText(classes[maxPos]);
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < classes.length; i++) {
                s.append(String.format("%s: %.4f\n", classes[i], confidences[i]));
            }

            numberResult.setText(confidences[maxPos] + "");
            //numberResult.setText( s.toString() );
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        btnTakePic = findViewById(R.id.picture_button);
        imageview = (ImageView) findViewById(R.id.current_photo);
        numberResult = (TextView) findViewById(R.id.results);
        translationResult = (TextView) findViewById(R.id.translated);
        //MyModel myModel = new MyModel.newInstance(context, options);
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mgetContent.launch("image/*");
            }
        });

     /*   Button btn;
        // Do you have to train and deploy your own model?
        // Alternative, use Google API? It maybe free and is easy. But requires Internet.
        btn.setPadding();
    }
*/

  /*  private void openCamera() {
        //ContentValues values = new ContentValues();
        //values.put(MediaStore.Images.Media.TITLE, "New Picture");
        //  values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values));
        ActivityResultContract.SynchronousResult result = ActivityResultContract.forResult(intent);
        startActivityForResult(intent, 1);
    }
    //register result contract - takes in a URI for where to store the image and returns a success boolean
    private void takePictureCallback =
            registerForActivityResult(new ActivityResultContracts.TakePicture()) { successful ->
        if (successful) {
            //load image from provided URI
        } else {
            //show user an error
        }
    }

    //take picture providing the URI you wish to save it to
    private void onTakePictureClick(){
        takePictureCallback.launch(Uri.parse("myuri"))
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //open camera and get image
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            imageview.setImageBitmap(photo);
            Log.d("Camera", photo.toString());
            doInference();
        }



    }

    private void doInference() {
        //uri to bitmap
        Drawable drawable = imageview.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        //list of results

        List results = classifier.recognizeImage(bitmap);
        //display results
        String result = "";
        for (Object res : results) {
            result += res + "\n";
            numberResult.setText(result);
        }
        //classifier
        //classifier classifier = new classifier();
       // classifier.classify(bitmap);

    }*/
    }
}