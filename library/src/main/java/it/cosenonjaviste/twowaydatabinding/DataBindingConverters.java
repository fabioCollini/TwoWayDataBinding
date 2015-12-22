/*
 *  Copyright 2015 Fabio Collini.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.cosenonjaviste.twowaydatabinding;

import android.databinding.BindingAdapter;
import android.support.v4.util.Pair;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class DataBindingConverters {

    @BindingAdapter({"app:binding"})
    public static void bindEditText(EditText view, final ObservableString observableString) {
        Pair<ObservableString, TextWatcherAdapter> pair = (Pair) view.getTag(R.id.bound_observable);
        if (pair == null || pair.first != observableString) {
            if (pair != null) {
                view.removeTextChangedListener(pair.second);
            }
            TextWatcherAdapter watcher = new TextWatcherAdapter() {
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    observableString.set(s.toString());
                }
            };
            view.setTag(R.id.bound_observable, new Pair<>(observableString, watcher));
            view.addTextChangedListener(watcher);
        }
        String newValue = observableString.get();
        if (!view.getText().toString().equals(newValue)) {
            view.setText(newValue);
        }
    }

    @BindingAdapter({"app:binding"})
    public static void bindRadioGroup(RadioGroup view, final ObservableString observableString) {
        if (view.getTag(R.id.bound_observable) != observableString) {
            view.setTag(R.id.bound_observable, observableString);
            view.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                    for (int i = 0; i < group.getChildCount(); i++) {
                        final View child = group.getChildAt(i);
                        if (checkedId == child.getId()) {
                            observableString.set(child.getTag().toString());
                            break;
                        }
                    }
                }
            });
        }
        String newValue = observableString.get();
        for (int i = 0; i < view.getChildCount(); i++) {
            final View child = view.getChildAt(i);
            if (child.getTag().toString().equals(newValue)) {
                ((RadioButton) child).setChecked(true);
                break;
            }
        }
    }

    @BindingAdapter({"app:binding"})
    public static void bindCheckBox(CheckBox view, final ObservableBoolean observableBoolean) {
        if (observableBoolean != null) {
            if (view.getTag(R.id.bound_observable) != observableBoolean) {
                view.setTag(R.id.bound_observable, observableBoolean);
                view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        observableBoolean.set(isChecked);
                    }
                });
            }
            boolean newValue = observableBoolean.get();
            if (view.isChecked() != newValue) {
                view.setChecked(newValue);
            }
        }
    }

    @BindingAdapter({"app:textHtml"})
    public static void bindHtmlText(TextView view, String text) {
        if (text != null) {
            view.setText(Html.fromHtml(text));
        } else {
            view.setText("");
        }
    }

    @BindingAdapter({"app:visibleOrGone"})
    public static void bindVisibleOrGone(View view, boolean b) {
        view.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter({"app:visible"})
    public static void bindVisible(View view, boolean b) {
        view.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter({"app:onClick"})
    public static void bindOnClick(View view, final Runnable listener) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.run();
            }
        });
    }
}
