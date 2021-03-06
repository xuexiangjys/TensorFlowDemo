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

package com.xuexiang.tensorflowdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xuexiang.tensorflowdemo.core.tflite.Recognition;

import java.util.List;

/**
 * 识别结果显示
 *
 * @author xuexiang
 * @since 2020-02-13 09:53
 */
public class RecognitionScoreView extends View implements ResultsView {
    private static final float TEXT_SIZE_DIP = 16;
    private final Paint fgPaint;
    private final Paint bgPaint;
    private List<Recognition> results;

    public RecognitionScoreView(final Context context, final AttributeSet set) {
        super(context, set);

        float textSizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        fgPaint = new Paint();
        fgPaint.setTextSize(textSizePx);
        bgPaint = new Paint();
        bgPaint.setColor(0xcc4285f4);
    }

    @Override
    public void setResults(final List<Recognition> results) {
        this.results = results;
        postInvalidate();
    }

    @Override
    public void onDraw(final Canvas canvas) {
        final int x = 10;
        int y = (int) (fgPaint.getTextSize() * 1.5f);

        canvas.drawPaint(bgPaint);

        if (results != null) {
            for (final Recognition recognition : results) {
                canvas.drawText(recognition.getTitle() + ": " + recognition.getConfidence(), x, y, fgPaint);
                y += (int) (fgPaint.getTextSize() * 1.5f);
            }
        }
    }
}
