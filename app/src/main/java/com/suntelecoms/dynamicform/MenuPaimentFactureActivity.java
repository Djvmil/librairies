package com.suntelecoms.dynamicform;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suntelecoms.td.dynamic_form.models.MenuFacturierObject;

import java.util.ArrayList;

public class MenuPaimentFactureActivity extends Activity {

	private static final String LOG_TAG = MenuPaimentFactureActivity.class.getSimpleName();
	private Context _context;
	Button btnConfirmer;
	private Dialog dialog;
	String idSession, urlink, version, codeCaisse;
	boolean retry = false; // // pour des besoins de soumission multiple
	AlertDialog validateCodePaiementDialog;
	private static ProgressDialog prd;

	private RecyclerView recyclerView;
	private RecyclerViewMenuFacturier adapterFacturier;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_paiment_facture);

		recyclerView = findViewById(R.id.recyclerViewFacturiers);

		ArrayList<MenuFacturierObject> list = new ArrayList<>();
		list.add(new MenuFacturierObject("" + R.drawable.ic_action_account_balance_wallet,"Orange"));
		list.add(new MenuFacturierObject("" + R.drawable.ic_action_account_balance_wallet,"SDE"));
		list.add(new MenuFacturierObject("" + R.drawable.ic_action_account_balance_wallet,"SENELEC"));
		list.add(new MenuFacturierObject("" + R.drawable.ic_action_account_balance_wallet,"CANAL"));

		adapterFacturier = new RecyclerViewMenuFacturier(getApplicationContext(), list);

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapterFacturier);
 }

	public void onShowFacturier(View v) {
		// TODO: open and configure the activity corresponding to that facturier
		Intent intent = new Intent(MenuPaimentFactureActivity.this, FormulaireFacturierActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
	}



}
