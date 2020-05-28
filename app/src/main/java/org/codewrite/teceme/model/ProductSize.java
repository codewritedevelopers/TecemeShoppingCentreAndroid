/*
 * Copyright (c) 2020 CODEWRITE.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codewrite.teceme.model;

import androidx.annotation.NonNull;

/**
 * Created by ERIC MENSAH on 5/30/2020.
 */

public class ProductSize {
    @NonNull
   private String size;
    private boolean selected;

    public ProductSize(@NonNull String size) {
        this.size = size;
        this.selected= false;
    }

    @NonNull
    public String getSize() {
        return size;
    }

    public void setSize(@NonNull String size) {
        this.size = size;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
