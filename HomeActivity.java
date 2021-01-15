ackage com.example.shoppingsaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingsaver.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FloatingActionButton btn_fab;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;

    private TextView totalsumResult;

    //Global variables..

    private String type;
    private int amount;
    private String note;
    private String post_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Shopping List");

        totalsumResult = findViewById(R.id.total_ammount);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        String uId = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uId);

        mDatabase.keepSynced(true);

        recyclerView = findViewById(R.id.recycler_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //Total sum number

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int totalammount = 0;

                for (DataSnapshot snap:dataSnapshot.getChildren()){
                    Data data = snap.getValue(Data.class);

                    totalammount += data.getAmount();

                    String sttotal = String.valueOf(totalammount);

                    totalsumResult.setText(sttotal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_fab = findViewById(R.id.fab);

        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });
    }

    private void customDialog() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View myview = inflater.inflate(R.layout.input_data, null);

        AlertDialog dialog = mydialog.create();

        dialog.setView(myview);

        EditText type = myview.findViewById(R.id.edt_type);
        EditText amount = myview.findViewById(R.id.edt_amount);
        EditText note = myview.findViewById(R.id.edt_note);
        Button btnSave =  myview.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                String mType = type.getText().toString().trim();
                String mAmount = amount.getText().toString().trim();
                String mNote = note.getText().toString().trim();

                int Famount = Integer.parseInt(mAmount);

                if (TextUtils.isEmpty(mType)){
                    type.setError("This Field is required....");
                    return;
                }

                if (TextUtils.isEmpty(mAmount)){
                    amount.setError("This Field is required....");
                    return;
                }

                if (TextUtils.isEmpty(mNote)){
                    note.setError("This Field is required....");
                    return;
                }

                String id = mDatabase.push().getKey();

                String date = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(mType,Famount,mNote,date,id);

                mDatabase.child(id).setValue(data);

                Toast.makeText(getApplicationContext(), "Shopping List Updated", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.item_data,
                        MyViewHolder.class,
                        mDatabase
                )
        {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, Data model, int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setAmmount(model.getAmount());

                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        post_key= getRef(position).getKey();
                        type = model.getType();
                        note = model.getNote();
                        amount = model.getAmount();

                        updateData();
                    }
                });

            }
        };
         recyclerView.setAdapter(adapter);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View myview;

        public MyViewHolder(View itemView) {
            super(itemView);
            myview = itemView;
        }

        public void setType(String type) {
            TextView mType = myview.findViewById(R.id.type);
            mType.setText(type);
        }

        public void setNote(String note) {
            TextView mNote = myview.findViewById(R.id.note);
            mNote.setText(note);
        }

        public void setDate(String date) {
            TextView mDate = myview.findViewById(R.id.date);
            mDate.setText(date);
        }

        public void setAmmount(int ammount){
            TextView mAmount = myview.findViewById(R.id.amount);
            String stam = String.valueOf(ammount);
            mAmount.setText(stam);
    }

}



public void updateData() {
    AlertDialog.Builder mydialog = new AlertDialog.Builder(HomeActivity.this);
    LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);

    View mView = inflater.inflate(R.layout.update_inputfield, null);

    AlertDialog dialog = mydialog.create();

    dialog.setView(mView);

    final EditText edt_Type = mView.findViewById(R.id.edt_type_upd);
    final EditText edt_Amount = mView.findViewById(R.id.edt_amount_upd);
    final EditText edt_Note = mView.findViewById(R.id.edt_note_upd);

    edt_Type.setText(type);
    edt_Type.setSelection(type.length());

    edt_Amount.setText(String.valueOf(amount));
    edt_Amount.setSelection(String.valueOf(amount).length());

    edt_Note.setText(note);
    edt_Note.setSelection(note.length());

    Button btnUpdate = mView.findViewById(R.id.btn_save_UPD);
    Button btnDelete = mView.findViewById(R.id.btn_delete_UPD);

    btnUpdate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            type = edt_Type.getText().toString().trim();

            String mAmount = String.valueOf(amount);

            mAmount = edt_Amount.getText().toString().trim();

            note = edt_Note.getText().toString().trim();

            int intamount = Integer.parseInt(mAmount);

            String date = DateFormat.getDateInstance().format(new Date());

            Data data = new Data(type, intamount, note, date, post_key);

            mDatabase.child(post_key).setValue(data);

            dialog.dismiss();

        }
    });

    btnDelete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mDatabase.child(post_key).removeValue();
            dialog.dismiss();

        }
    });


    dialog.show();
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_Home:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                break;

            case R.id.action_Weather:
                //startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
                break;

            case R.id.action_Location:
                //startActivity(new Intent(getApplicationContext(), MapActivity.class));
                break;

            case R.id.log_out:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
