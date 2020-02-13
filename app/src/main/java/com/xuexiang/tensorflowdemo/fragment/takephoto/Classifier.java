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

package com.xuexiang.tensorflowdemo.fragment.takephoto;

import android.graphics.Bitmap;

import java.util.List;

/**
 * @author xuexiang
 * @since 2020-02-12 12:54
 */
public interface Classifier {

    class Recognition {
        /**
         * A unique identifier for what has been recognized. Specific to the class, not the instance of
         * the object.
         */
        private final String id;

        /**
         * 识别名
         */
        private final String title;

        /**
         * 模型是否具有量化权重或浮动权重。
         */
        private final boolean quantized;

        /**
         * 可信度
         */
        private final Float confidence;

        public Recognition(final String id, final String title, final Float confidence, final boolean quantized) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
            this.quantized = quantized;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Float getConfidence() {
            return confidence;
        }

        @Override
        public String toString() {
            String resultString = "";
            if (id != null) {
                resultString += "[" + id + "] ";
            }

            if (title != null) {
                resultString += title + " ";
            }

            if (confidence != null) {
                resultString += String.format("(%.1f%%) ", confidence * 100.0f);
            }

            return resultString.trim();
        }
    }


    /**
     * 识别图片
     *
     * @param bitmap
     * @return
     */
    List<Recognition> recognizeImage(Bitmap bitmap);

    /**
     * 停止
     */
    void close();
}
