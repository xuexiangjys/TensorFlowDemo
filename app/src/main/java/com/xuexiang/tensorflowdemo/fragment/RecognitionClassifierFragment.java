/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.tensorflowdemo.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import com.xuexiang.tensorflowdemo.R;
import com.xuexiang.tensorflowdemo.core.BaseFragment;
import com.xuexiang.tensorflowdemo.core.classifier.Classifier;
import com.xuexiang.tensorflowdemo.core.classifier.TensorFlowImageClassifier;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xpage.annotation.Page;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 这个只是一个空壳Fragment，只是用于演示而已
 *
 * @author xuexiang
 * @since 2019-07-08 00:52
 */
@Page(name = "图片识物分类")
public class RecognitionClassifierFragment extends BaseFragment {

    private static final String MODEL_PATH = "mobilenet_quant_v1_224.tflite";
    /**
     * 模型是否具有量化权重或浮动权重
     */
    private static final boolean QUANTIZED = true;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 224;

    @BindView(R.id.camera_view)
    CameraView cameraView;
    @BindView(R.id.iv_result)
    ImageView ivResult;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.btn_recognition)
    Button btnRecognition;

    private Classifier mClassifier;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recognition_classifier;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        initCamera();
        initTensorFlowAndLoadModel();
    }

    private void initCamera() {
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
                ivResult.setImageBitmap(bitmap);
                final List<Classifier.Recognition> results = mClassifier.recognizeImage(bitmap);
                tvResult.setText(results.toString());
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }


    private void initTensorFlowAndLoadModel() {
        mExecutor.execute(() -> {
            try {
                mClassifier = TensorFlowImageClassifier.create(
                        getActivity().getAssets(),
                        MODEL_PATH,
                        LABEL_PATH,
                        INPUT_SIZE,
                        QUANTIZED);
                makeButtonVisible();
            } catch (final Exception e) {
                throw new RuntimeException("Error initializing TensorFlow!", e);
            }
        });
    }

    @MainThread
    private void makeButtonVisible() {
        btnRecognition.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.btn_toggle_camera, R.id.btn_recognition})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_toggle_camera:
                cameraView.toggleFacing();
                break;
            case R.id.btn_recognition:
                cameraView.captureImage();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    public void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutor.execute(() -> mClassifier.close());
    }
}
