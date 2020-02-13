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

import android.view.View;

import com.xuexiang.tensorflowdemo.R;
import com.xuexiang.tensorflowdemo.core.BaseFragment;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xutil.app.ActivityUtils;

import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2020-02-12 15:48
 */
@Page(name = "图像分类")
public class ImageClassificationFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_classification;
    }

    @Override
    protected void initViews() {

    }

    @SingleClick
    @OnClick(R.id.btn_start)
    public void onViewClicked(View view) {
        ActivityUtils.startActivity(ImageClassifierActivity.class);
    }
}
