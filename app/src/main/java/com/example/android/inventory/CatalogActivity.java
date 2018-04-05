package com.example.android.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory.data.InventoryContract.InventoryEntry;

/**
 * Displays list of inventory that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the inventory data loader
     */
    private static final int INVENTORY_LOADER = 0;

    /**
     * Adapter for the ListView
     */
    InventoryCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the inventory data
        ListView inventoryListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        inventoryListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of inventory data in the Cursor.
        // There is no inventory data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new InventoryCursorAdapter(this, null);
        inventoryListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int btnid = R.id.button_sale;
                TextView tView = (TextView) findViewById(R.id.button_sale);
                tView.isSelected();
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                // Form the content URI that represents the specific inventory that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link InventoryEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.inventory/finalinventory/2"
                // if the inventory with ID 2 was clicked on.
                Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentInventoryUri);
                intent.putExtra("currentInventoryUri", currentInventoryUri);
                // Launch the {@link EditorActivity} to display the data for the current inventory.
                startActivity(intent);
            }
        });

       /* TextView saleTextView = (TextView) findViewById(R.id.button_sale);
        saleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventorySale();
            }
        });*/

        // Kick off the loader
        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }

    public void inventorySale (){
        Toast toast = Toast.makeText(getApplicationContext(),"did it", Toast.LENGTH_LONG);
        toast.show();
    }
    /**
     * Helper method to insert hardcoded inventory data into the database. For debugging purposes only.
     */
    private void insertInventory() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's inventory attributes are the values.
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_STOCK_NAME, "Headphones");
        values.put(InventoryEntry.COLUMN_STOCK_PRICE, 50);
        values.put(InventoryEntry.COLUMN_STOCK_QUANTITY, 0);
        values.put(InventoryEntry.COLUMN_STOCK_SUPPLIER_NAME, "Skull Candy");
        values.put(InventoryEntry.COLUMN_STOCK_SUPPLER_NUMBER, "5556458974");

        Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

        ContentValues values2 = new ContentValues();
        values2.put(InventoryEntry.COLUMN_STOCK_NAME, "Glasses");
        values2.put(InventoryEntry.COLUMN_STOCK_PRICE, 5);
        values2.put(InventoryEntry.COLUMN_STOCK_QUANTITY, 20);
        values2.put(InventoryEntry.COLUMN_STOCK_SUPPLIER_NAME, "RayBans");
        values2.put(InventoryEntry.COLUMN_STOCK_SUPPLER_NUMBER, "6498742626");

        Uri newUri2 = getContentResolver().insert(InventoryEntry.CONTENT_URI, values2);
    }

    /**
     * Helper method to delete all inventory in the database.
     */
    private void deleteAllInventory() {
        int rowsDeleted = getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from inventory database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertInventory();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllInventory();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {InventoryEntry._ID,
                InventoryEntry.COLUMN_STOCK_NAME,
                InventoryEntry.COLUMN_STOCK_PRICE,
                InventoryEntry.COLUMN_STOCK_QUANTITY};

        return new CursorLoader(this,
                InventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link InventoryCursorAdapter} with this new cursor containing updated inventory data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

}
