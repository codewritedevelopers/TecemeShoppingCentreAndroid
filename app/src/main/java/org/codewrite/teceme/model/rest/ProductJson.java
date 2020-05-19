package org.codewrite.teceme.model.rest;

import androidx.annotation.NonNull;

import org.codewrite.teceme.model.holder.Product;

public class ProductJson extends Product {
    public ProductJson(@NonNull Integer product_id, String product_name, String product_color,
                       String product_weight, String product_size, String product_code,
                       String product_desc, Integer product_category_id, String product_date_created,
                       Boolean product_access, String product_img_uri) {

        super(product_id, product_name, product_color, product_weight, product_size, product_code,
                product_desc, product_category_id, product_date_created, product_access, product_img_uri);
    }
}
