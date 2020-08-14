/**
  * Copyright 2017 JessYan
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.hnsh.dialogue.ui;

import android.app.Activity;
import android.os.Bundle;

import com.dosmono.universal.activity.BaseActivity;

/**
 * ================================================
 * 框架要求框架中的每个 {@link Activity} 都需要实现此类,以满足规范
 *
 * @see BaseActivity
 * Created by JessYan on 26/04/2017 21:42
 * Contact with <mailto:jess.yan.effort@gmail.com>
 * Follow me on <https://github.com/JessYanCoding>
 * ================================================
 */
public interface IActivity {

    /**
     * 提供 AppComponent(提供所有的单例对象)给实现类,进行 Component 依赖
     *
     */
    void setupActivityComponent();

    /**
     * 是否使用 {@link org.greenrobot.eventbus.EventBus}
     *
     * @return
     */

    /**
     * 初始化 View,如果initView返回0,框架则不会调用{@link Activity#setContentView(int)}
     *
     * @param savedInstanceState
     * @return
     */
    int initContextView(Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void initData(Bundle savedInstanceState);
}
