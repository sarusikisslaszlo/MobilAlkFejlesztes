package com.example.healthcareservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceListActivity extends AppCompatActivity {

    private static final String LOG_TAG = ServiceListActivity.class.getName();
    private static final int RC_ADD_ITEM = 987;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<ServiceItem> mItemList;
    private ServiceItemAdapter mAdapter;
    private FrameLayout redCircle;
    private TextView contentTextView;
    private List<ServiceItem> list = new ArrayList<>();

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    private int queryLimit = 10;

    private NotificationHandler mNotificationHandler;

    private int gridNumber = 1;
    private int cartItems = 0;
    private boolean viewRow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();
        
        mAdapter = new ServiceItemAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");
        queryData(true);
        list.clear();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(powerReceiver, intentFilter);

        mNotificationHandler = new NotificationHandler(this);
    }

    BroadcastReceiver powerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action == null) {
                return;
            }

            switch (action) {
                case Intent.ACTION_POWER_CONNECTED:
                    queryLimit = mItemList.size();
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    queryLimit = 5;
                    break;

            }

            queryData(true);
        }
    };

    private void queryData(boolean active) {
        mItemList.clear();

        mItems.whereEqualTo("active", active).limit(queryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                ServiceItem item = documentSnapshot.toObject(ServiceItem.class);
                item.setId(documentSnapshot.getId());
                mItemList.add(item);
            }

            if(mItemList.size() == 0) {
                initializeData();
                queryData(true);
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    public void deleteItem(ServiceItem item) {
        DocumentReference ref = mItems.document(item._getId());

        ref.delete().addOnSuccessListener(success -> {
            Log.d(LOG_TAG, "Item is successfully deleted: " + item._getId());
        }).addOnFailureListener(failure -> {
            Toast.makeText(this, "Item " + item._getId() + " cannot be deleted.", Toast.LENGTH_LONG).show();
        });
        queryData(true);
        mNotificationHandler.cancel();
    }

    private void initializeData() {
        String[] itemsList = getResources().getStringArray(R.array.service_item_names);
        String[] itemsProvider = getResources().getStringArray(R.array.service_item_providers);
        String[] itemsCategory = getResources().getStringArray(R.array.service_item_categories);
        String[] itemsComment = getResources().getStringArray(R.array.service_item_comments);
        String[] itemsActive = getResources().getStringArray(R.array.service_item_actives);
        TypedArray itemsImageResource = getResources().obtainTypedArray(R.array.service_item_images);

        for(int i = 0; i< itemsList.length; i++) {
            List<String> categories = new ArrayList<>(Arrays.asList(itemsCategory[i].split(",")));
            mItems.add(new ServiceItem(
                    itemsList[i],
                    itemsProvider[i],
                    categories,
                    Boolean.parseBoolean(itemsActive[i]),
                    itemsComment[i],
                    itemsImageResource.getResourceId(i, 0)));
        }

        itemsImageResource.recycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.service_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(LOG_TAG, newText);
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_button:
                Log.d(LOG_TAG, "Log out clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.setting_button:
                Log.d(LOG_TAG, "Settings clicked!");
                return true;
            case R.id.saved_button:
                Log.d(LOG_TAG, "Bookmarks clicked!");
                return true;
            case R.id.view_selector:
                Log.d(LOG_TAG, "View selector clicked!");
                if(viewRow) {
                    changeSpanCount(item, R.drawable.ic_view_grid, 1);
                } else {
                    changeSpanCount(item, R.drawable.ic_view_row, 2);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
        viewRow = ! viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final  MenuItem alertMenuItem = menu.findItem(R.id.saved_button);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        contentTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(v -> onOptionsItemSelected(alertMenuItem));

        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon(ServiceItem item) {
        if(!list.contains(item)) {
            list.add(item);
            cartItems = (cartItems + 1);
            if(0 < cartItems) {
                contentTextView.setText(String.valueOf(cartItems));
            } else {
                contentTextView.setText("");
            }

            redCircle.setVisibility((cartItems > 0) ? View.VISIBLE : View.GONE);

            mNotificationHandler.send(item.getName());
            queryData(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReceiver);
    }

    public void addItem(View view) {
        Intent intent = new Intent(this, ItemOperationActivity.class);
        startActivityForResult(intent, RC_ADD_ITEM);
    }
}