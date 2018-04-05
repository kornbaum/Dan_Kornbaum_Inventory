
package com.example.android.inventory.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Inventory app.
 */
public final class InventoryContract {

    private InventoryContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.android.inventory";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_INVENTORY = "finalinventory";

    public static final class InventoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        public final static String TABLE_NAME = "finalinventory";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_STOCK_NAME = "name";

        public final static String COLUMN_STOCK_PRICE = "price";

        public final static String COLUMN_STOCK_QUANTITY = "quantity";

        public final static String COLUMN_STOCK_SUPPLIER_NAME = "supplier_name";

        public final static String COLUMN_STOCK_SUPPLER_NUMBER = "supplier_number";

    }

}

