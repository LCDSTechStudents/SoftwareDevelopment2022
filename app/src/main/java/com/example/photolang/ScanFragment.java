package com.example.photolang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photolang.data.tf.ModelResult;
import com.example.photolang.ml.Model;
import com.example.photolang.ui.flashcards.ResultFragment;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    //tensorflow data
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




    public ScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScanFragment.
     */

    public static ScanFragment newInstance() {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        btnTakePic = view.findViewById(R.id.button2);
        imageview = view.findViewById(R.id.imageView);
        numberResult = view.findViewById(R.id.textView2);
        translationResult = view.findViewById(R.id.textView3);
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startClassify();
            }
        });
        return view;
    }


    public void startClassify() {
        // Create the new Intent using the 'Send' action.
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mGetContent.launch(cameraIntent);
    }
    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Log.d("cameraActivity", "onActivityResult: " + result);
            try {
                Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                imageview.setImageBitmap(bitmap);
                int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
                bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);
                ModelResult mr = classify(bitmap);
                toResultFragment(mr);


            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    });

    private ModelResult classify(Bitmap bitmap) {

        try {
            Model model = Model.newInstance(getActivity().getApplicationContext());

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

            String[] classes = {
                    "Apple",
                    "Banana",
                    "beetroot",
                    "bell pepper",
                    "cabbage",
                    "carrot",
                    "cauliflower",
                    "chili pepper",
                    "corn",
                    "cucumber"
            };

            translationResult.setText(classes[maxPos]);
            StringBuilder s = new StringBuilder();

            for (int i = 0; i < classes.length; i++) {
                s.append(String.format("%s: %.4f\n", classes[i], confidences[i]));
            }

            numberResult.setText(confidences[maxPos] + "");
            ModelResult mr = new ModelResult(classes[maxPos], confidences[maxPos], bitmap);

            model.close();
            return mr;
        } catch (IOException e) {
            // TODO Handle the exception
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void toResultFragment(ModelResult mr) {
        ResultFragment rf = ResultFragment.newInstance(mr);
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, rf);
        ft.addToBackStack(null);
        ft.commit();
    }
}