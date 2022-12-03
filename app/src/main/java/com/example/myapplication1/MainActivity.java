package com.example.myapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication1.adapter.ListItem;
import com.example.myapplication1.adapter.MainAdapter;
import com.example.myapplication1.db.AppExecuter;
import com.example.myapplication1.db.MyDbManger;
import com.example.myapplication1.db.OnDataReceived;

import java.util.List;


public class MainActivity extends AppCompatActivity implements OnDataReceived {

    private MyDbManger myDbManger;
    private EditText edTitle, edDisc;
    private TextView tvTest;
    private RecyclerView rcView;
    private MainAdapter mainAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.id_search);
        SearchView sv =(SearchView)item.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                
                readFromDb(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        myDbManger = new MyDbManger(this);
        edTitle = findViewById(R.id.edTitle);
        edDisc = findViewById(R.id.edDesc);
        rcView = findViewById(R.id.rcView);
        mainAdapter = new MainAdapter(this);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        getItemTouchHelper().attachToRecyclerView(rcView);
        rcView.setAdapter(mainAdapter);
    }


    @Override
    protected void onResume() {

        super.onResume();
        myDbManger.openDb();
       readFromDb("");

    }

    public void onClickAdd(View view) {
        Intent i = new Intent(MainActivity.this, EditActivity.class);
        startActivity(i);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbManger.closeDb();
    }


public ItemTouchHelper getItemTouchHelper(){
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mainAdapter.removeItem(viewHolder.getAdapterPosition(), myDbManger);
            }
        });
}
private void readFromDb(final String text){
    AppExecuter.getInstance().getSubIO().execute(new Runnable() {
        @Override
        public void run() {
            myDbManger.getFromDb(text,MainActivity.this);
        }
    });
}

    @Override
    public void onReceived(List<ListItem> list) {
AppExecuter.getInstance().getMainIO().execute(new Runnable() {
    @Override
    public void run() {
mainAdapter.updateAdapter(list);
    }
});
    }
}