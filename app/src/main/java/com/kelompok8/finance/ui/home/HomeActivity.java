package com.kelompok8.finance.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok8.finance.MainActivity;
import com.kelompok8.finance.R;
import com.kelompok8.finance.adapter.DompetAdapter;
import com.kelompok8.finance.adapter.PengeluaranAdapter;
import com.kelompok8.finance.adapter.TransaksiAdapter;
import com.kelompok8.finance.database.DBHelper;
import com.kelompok8.finance.model.Dompet;
import com.kelompok8.finance.model.Pengeluaran;
import com.kelompok8.finance.model.User;
import com.kelompok8.finance.ui.profile.EditPasswordActivity;
import com.kelompok8.finance.ui.profile.EditProfileActivity;
import com.kelompok8.finance.ui.profile.ProfileActivity;
import com.kelompok8.finance.ui.stats.StatisticActivity;
import com.kelompok8.finance.ui.wallets.AddDompetActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPengeluaran, recyclerViewTransaksi, recyclerViewDompet;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayList<Pengeluaran> pengeluaranHolder;
    private ArrayList<Pengeluaran> pengeluaranHolder2;
    private ArrayList<Dompet> dompetHolder;
    private User user;
    private DBHelper dbHelper;
    private Uri imageUri;
    TextView emptyPengeluaran;
    TextView emptyTransaksi;

    int idUser;

    CircleImageView fotoProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        idUser = this.getSharedPreferences("login_session", 0).getInt("key_id", 0);
        dbHelper = new DBHelper(this);

        try {
            Cursor cursor = (Cursor) dbHelper.getUserLogin(idUser);
            cursor.moveToLast();
            user =new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
        }catch (Exception e){
            Log.e("error user", "Error:" + e.getMessage());
            return;
        }

        TextView pengeluaranShow = findViewById(R.id.pengeluaranShowAll);
        TextView transaksiShow = findViewById(R.id.transaksiShowAll);
        emptyPengeluaran = findViewById(R.id.pengeluaranNull);
        emptyTransaksi = findViewById(R.id.transaksiNull);
        fotoProfile = findViewById(R.id.profile_image);

        TextView username = findViewById(R.id.textUsernameHome);
        username.setText(user.getUsername());


        ImageView addDompet = findViewById(R.id.dompetkuAdd);
        Group profile = findViewById(R.id.groupProfile);

        String foto = user.getFoto();

        if (foto == null){
            fotoProfile.setImageResource(R.drawable.blank_user);
        }
        else {
            imageUri = Uri.parse(foto);
            fotoProfile.setImageURI(imageUri);
        }

        fotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent1);
            }
        });

        pengeluaranShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, StatisticActivity.class));
            }
        });
        transaksiShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, StatisticActivity.class));
            }
        });
        addDompet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddDompetActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    public void init(){

        pengeluaranHolder = new ArrayList<>();
        pengeluaranHolder2 = new ArrayList<>();
        dompetHolder = new ArrayList<>();

        recyclerViewPengeluaran = (RecyclerView) findViewById(R.id.listPengeluaran);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPengeluaran.setLayoutManager(horizontalLayoutManagaer);

        recyclerViewDompet = (RecyclerView) findViewById(R.id.listDompet);
        LinearLayoutManager horizontalLayoutDompet = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDompet.setLayoutManager(horizontalLayoutDompet);

        recyclerViewTransaksi = (RecyclerView) findViewById(R.id.listTransaksi);
        recyclerViewTransaksi.setLayoutManager(new LinearLayoutManager(this));

        Cursor cursor = new DBHelper(this).readPengeluaran();

        if (cursor.getCount()  == 0) {
            emptyPengeluaran.setVisibility(View.VISIBLE);
            emptyTransaksi.setVisibility(View.VISIBLE);
        }

//        if (cursor.getCount() > 0) {
//            emptyView.setVisibility(View.GONE);
//        }
//        else {
//            emptyView.setVisibility(View.VISIBLE);
//        }

        while(cursor.moveToNext()){
            Pengeluaran pengeluaran = new Pengeluaran(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6));
            pengeluaranHolder.add(pengeluaran);
        }

        Cursor cursor1 = new DBHelper(this).readPengeluaranGByCategory();
        while(cursor1.moveToNext()){
            Pengeluaran pengeluaran = new Pengeluaran(cursor1.getInt(0),
                    cursor1.getString(1),
                    cursor1.getInt(2),
                    cursor1.getInt(3),
                    cursor1.getString(4),
                    cursor1.getString(5),
                    cursor1.getInt(6));
            pengeluaranHolder2.add(pengeluaran);
        }

        DompetAdapter dompetAdapter = new DompetAdapter(HomeActivity.this, dbHelper.getDompet(idUser));
        recyclerViewDompet.setAdapter(dompetAdapter);

        PengeluaranAdapter pengeluaranAdapter = new PengeluaranAdapter(pengeluaranHolder2, HomeActivity.this, sqLiteDatabase);
        recyclerViewPengeluaran.setAdapter((RecyclerView.Adapter) pengeluaranAdapter);


        TransaksiAdapter transaksiAdapter = new TransaksiAdapter(pengeluaranHolder, HomeActivity.this, sqLiteDatabase);
        recyclerViewTransaksi.setAdapter((RecyclerView.Adapter) transaksiAdapter);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set title dialog
        alertDialogBuilder.setTitle("Kamu yakin mau keluar dari aplikasi?");

        // Set pesan dari dialog
        alertDialogBuilder
                .setMessage("Pilih Ya jika ingin keluar dari aplikasi")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // Jika tombol diklik, maka akan menutup activity ini
                        HomeActivity.this.finishAffinity();
                    }
                })
                .setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Jika tombol ini diklik, akan menutup dialog
                        dialog.cancel();
                    }
                });

        // Membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Menampilkan alert dialog
        alertDialog.show();
    }
}