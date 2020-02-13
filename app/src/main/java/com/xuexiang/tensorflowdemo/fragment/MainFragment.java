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

import android.view.KeyEvent;

import com.xuexiang.tensorflowdemo.core.BaseContainerFragment;
import com.xuexiang.tensorflowdemo.fragment.gesture_recognition.GestureRecognitionFragment;
import com.xuexiang.tensorflowdemo.fragment.image_classification.ImageClassificationFragment;
import com.xuexiang.tensorflowdemo.fragment.object_detection.ObjectDetectionFragment;
import com.xuexiang.tensorflowdemo.fragment.takephoto.TakePhotoRecognitionFragment;
import com.xuexiang.tensorflowdemo.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.ClickUtils;

/**
 * @author xuexiang
 * @since 2018/11/7 下午1:16
 */
@Page(name = "TensorFlow案例演示", anim = CoreAnim.none)
public class MainFragment extends BaseContainerFragment implements ClickUtils.OnClick2ExitListener {

    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                //此处填写fragment
                TakePhotoRecognitionFragment.class,
                ImageClassificationFragment.class,
                ObjectDetectionFragment.class,
                GestureRecognitionFragment.class
        };
    }

    @Override
    protected TitleBar initTitle() {
        return super.initTitle().setLeftClickListener(view -> ClickUtils.exitBy2Click(2000, this));
    }


    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click(2000, this);
        }
        return true;
    }

    @Override
    public void onRetry() {
        XToastUtils.toast("再按一次退出程序");
    }

    @Override
    public void onExit() {
        XUtil.get().exitApp();
    }
}
