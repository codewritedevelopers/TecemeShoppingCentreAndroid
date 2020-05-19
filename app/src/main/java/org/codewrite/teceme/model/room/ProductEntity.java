package org.codewrite.teceme.model.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.codewrite.teceme.model.holder.Product;

@Entity(tableName = "product_table")
public class ProductEntity extends Product {
    public ProductEntity(@NonNull Integer product_id, String product_name, String product_color,
                         String product_weight, String product_size, String product_code,
                         String product_desc, Integer product_category_id, String product_date_created,
                         Boolean product_access, String product_img_uri) {

        super(product_id, product_name, product_color, product_weight,
                product_size, product_code, product_desc, product_category_id,
                product_date_created, product_access, product_img_uri);
    }
}
