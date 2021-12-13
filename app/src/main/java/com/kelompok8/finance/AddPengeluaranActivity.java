package com.kelompok8.finance;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kelompok8.finance.database.DBHelper;
import com.kelompok8.finance.model.Pengeluaran;
import com.kelompok8.finance.ui.stats.StatisticActivity;

import es.dmoral.toasty.Toasty;

public class AddPengeluaranActivity extends AppCompatActivity {
    EditText jumlahUang, catatan;
    private DBHelper db;
    private Pengeluaran pengeluaran;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pengeluaran);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar().setTitle("Tambah Pengeluaran");

        jumlahUang = findViewById(R.id.amountInput);
        catatan = findViewById(R.id.noteInput);

        btnSubmit = (Button) findViewById(R.id.button_update);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pengeluaran = new Pengeluaran(0, 1, 1,
                        Integer.parseInt(jumlahUang.getText().toString()),
                        catatan.getText().toString(),
                        "14 Desember 2021");

                saveDataToDB();

                Intent intent = new Intent(AddPengeluaranActivity.this, StatisticActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveDataToDB(){
        db = new DBHelper(this);
        db.insertPengeluaran(pengeluaran.getIdKategori(),
                pengeluaran.getIdUser(),
                pengeluaran.getJumlahPengeluaran(),
                pengeluaran.getCatatan(),
                pengeluaran.getTanggal());
    }
}