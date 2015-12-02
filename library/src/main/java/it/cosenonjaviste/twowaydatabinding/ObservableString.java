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

import android.databinding.BaseObservable;
import android.databinding.BindingConversion;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ObservableString extends BaseObservable implements Parcelable, Serializable {
    static final long serialVersionUID = 1L;
    private String mValue;
    public static final Creator<ObservableString> CREATOR = new Creator<ObservableString>() {
        public ObservableString createFromParcel(Parcel source) {
            return new ObservableString(source.readString());
        }

        public ObservableString[] newArray(int size) {
            return new ObservableString[size];
        }
    };

    public ObservableString(String value) {
        this.mValue = value;
    }

    public ObservableString() {
    }

    public String get() {
        return this.mValue;
    }

    public void set(String value) {
        if (!equals(value, this.mValue)) {
            this.mValue = value;
            this.notifyChange();
        }

    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mValue);
    }

    public boolean isEmpty() {
        return mValue == null || mValue.isEmpty();
    }

    @BindingConversion
    public static String convertToString(ObservableString s) {
        return s.get();
    }
}