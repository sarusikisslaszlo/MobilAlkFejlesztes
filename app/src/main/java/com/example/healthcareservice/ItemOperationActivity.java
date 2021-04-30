package com.example.healthcareservice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ItemOperationActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegisterActivity.class.getName();

    EditText serviceNameEditText;
    EditText serviceProviderEditText;
    CheckBox privateService;
    CheckBox publicService;
    SwitchMaterial active;
    EditText comment;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_service_item);

        serviceNameEditText = findViewById(R.id.serviceNameET);
        serviceProviderEditText = findViewById(R.id.serviceProviderET);
        privateService = findViewById(R.id.categoryPrivateService);
        publicService = findViewById(R.id.categoryPublicService);
        active = findViewById(R.id.switchActive);
        comment = findViewById(R.id.commentItem);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");
    }

    public void create(View view) {
        String serviceName = serviceNameEditText.getText().toString();
        String providedBy = serviceProviderEditText.getText().toString();
        int itemsImageResource = getResources().obtainTypedArray(R.array.service_item_images).getResourceId(0, 0);
        List<String> category = new ArrayList<>();
        if (privateService.isChecked()) {
            category.add("Magán");
        }
        if(publicService.isChecked()) {
            category.add("Közösségi");
        }

        boolean activeSwitch = active.isChecked();
        String commentService = comment.getText().toString();

        mItems.add(new ServiceItem(
                serviceName,
                providedBy,
                category,
                activeSwitch,
                commentService,
                itemsImageResource
        )).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                Log.d(LOG_TAG, "Item created successfully.");
                startSearchForServices();
            } else {
                Log.d(LOG_TAG, "Item wasn't created successfully.");
                Toast.makeText(ItemOperationActivity.this, "Item wasn't created successfully: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void cancel(View view) {
    }

    private void startSearchForServices() {
        Intent intent = new Intent(this, ServiceListActivity.class);
        startActivity(intent);
    }
}
