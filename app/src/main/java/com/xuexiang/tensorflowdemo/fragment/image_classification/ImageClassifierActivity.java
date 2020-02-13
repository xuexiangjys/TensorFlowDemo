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

package com.xuexiang.tensorflowdemo.fragment.image_classification;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.SystemClock;
import android.util.Size;
import android.util.TypedValue;

import com.xuexiang.tensorflowdemo.R;
import com.xuexiang.tensorflowdemo.fragment.image_classification.tflite.Classifier;
import com.xuexiang.tensorflowdemo.utils.BorderedText;
import com.xuexiang.tensorflowdemo.utils.XToastUtils;
import com.xuexiang.xutil.common.logger.Logger;

import java.io.IOException;
import java.util.List;

/**
 * 图像实时分类
 *
 * @author xuexiang
 * @since 2020-02-13 14:54
 */
public class ImageClassifierActivity extends CameraActivity implements OnImageAvailableListener {
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
    private static final float TEXT_SIZE_DIP = 10;
    private Bitmap rgbFrameBitmap = null;
    private long lastProcessingTimeMs;
    private Integer sensorOrientation;
    private Classifier classifier;
    /**
     * Input image size of the model along x axis.
     */
    private int imageSizeX;
    /**
     * Input image size of the model along y axis.
     */
    private int imageSizeY;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera_connection;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        BorderedText borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        recreateClassifier(getModel(), getDevice(), getNumThreads());
        if (classifier == null) {
            Logger.e("No classifier on preview!");
            return;
        }

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        Logger.i(String.format("Camera orientation relative to screen canvas: %d", sensorOrientation));

        Logger.i(String.format("Initializing at size %dx%d", previewWidth, previewHeight));
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
    }

    @Override
    protected void processImage() {
        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);
        final int cropSize = Math.min(previewWidth, previewHeight);

        runInBackground(
                () -> {
                    if (classifier != null) {
                        final long startTime = SystemClock.uptimeMillis();
                        final List<Classifier.Recognition> results =
                                classifier.recognizeImage(rgbFrameBitmap, sensorOrientation);
                        lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
                        Logger.v(String.format("Detect: %s", results));

                        runOnUiThread(
                                () -> {
                                    showResultsInBottomSheet(results);
                                    showFrameInfo(previewWidth + "x" + previewHeight);
                                    showCropInfo(imageSizeX + "x" + imageSizeY);
                                    showCameraResolution(cropSize + "x" + cropSize);
                                    showRotationInfo(String.valueOf(sensorOrientation));
                                    showInference(lastProcessingTimeMs + "ms");
                                });
                    }
                    readyForNextImage();
                });
    }

    @Override
    protected void onInferenceConfigurationChanged() {
        if (rgbFrameBitmap == null) {
            // Defer creation until we're getting camera frames.
            return;
        }
        final Classifier.Device device = getDevice();
        final Classifier.Model model = getModel();
        final int numThreads = getNumThreads();
        runInBackground(() -> recreateClassifier(model, device, numThreads));
    }

    private void recreateClassifier(Classifier.Model model, Classifier.Device device, int numThreads) {
        if (classifier != null) {
            Logger.d("Closing classifier.");
            classifier.close();
            classifier = null;
        }
        if (device == Classifier.Device.GPU && model == Classifier.Model.QUANTIZED) {
            Logger.d("Not creating classifier: GPU doesn't support quantized models.");
            XToastUtils.toast("GPU does not yet supported quantized models.");
            return;
        }
        try {
            Logger.d(String.format(
                    "Creating classifier (model=%s, device=%s, numThreads=%d)", model, device, numThreads));
            classifier = Classifier.create(this, model, device, numThreads);
        } catch (IOException e) {
            Logger.e("Failed to create classifier.", e);
        }

        // Updates the input image size.
        imageSizeX = classifier.getImageSizeX();
        imageSizeY = classifier.getImageSizeY();
    }
}
