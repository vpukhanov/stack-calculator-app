package ru.pukhanov.stackcalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String SPREFS_TAG = "SC_SHARED_PREFS";
    private static final String SPREFS_KEY_VALUES = "SC_SHARED_PREFS_VALUES";
    @BindViews({
            R.id.btn_delete, R.id.btn_clear, R.id.btn_sin, R.id.btn_cos, R.id.btn_tg,
            R.id.btn_ctg
    })
    protected List<View> unaryOperationButtons;
    @BindViews({
            R.id.btn_swap, R.id.btn_add, R.id.btn_subtract, R.id.btn_multiply, R.id.btn_divide,
            R.id.btn_power, R.id.btn_root, R.id.btn_log
    })
    protected List<View> binaryOperationButtons;
    @BindView(R.id.btn_divide)
    protected Button btnDivide;
    @BindView(R.id.btn_root)
    protected Button btnRoot;
    @BindView(R.id.btn_log)
    protected Button btnLog;
    @BindView(R.id.lv_main)
    protected ListView lvMain;
    private Calculator calculator;
    private Dialoguer dialoguer;
    private ArrayAdapter<Double> lvMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        calculator = new Calculator(loadNumbersList());
        calculator.setOnUpdateListener(new Calculator.OnUpdateListener() {
            @Override
            public void OnUpdate(List<Double> numbers) {
                lvMainAdapter.notifyDataSetChanged();
                updateButtonsState();
                saveNumbersList(numbers);
            }
        });

        dialoguer = new Dialoguer();

        setupListView();
        updateButtonsState();
    }

    private void setupListView() {
        lvMainAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                calculator.getNumbers()
        );
        lvMain.setAdapter(lvMainAdapter);
        lvMain.setSelection(0);
    }

    private void updateButtonsState() {
        boolean isUnaryOperationAvailable = calculator.isUnaryOperationAvailable();
        boolean isBinaryOperationAvailable = calculator.isBinaryOperationAvailable();

        for (View ubtn : unaryOperationButtons) {
            setButtonEnabled(ubtn, isUnaryOperationAvailable);
        }
        for (View bbtn : binaryOperationButtons) {
            setButtonEnabled(bbtn, isBinaryOperationAvailable);
        }

        btnDivide.setEnabled(calculator.isDivisionAvailable());
        btnRoot.setEnabled(calculator.isRootAvailable());
        btnLog.setEnabled(calculator.isLogAvailable());
    }

    private void setButtonEnabled(View btn, boolean enabled) {
        if (btn instanceof ImageButton) {
            ((ImageButton) btn).setImageAlpha(enabled ? 0xFF : 0x3F);
        }
        btn.setEnabled(enabled);
    }

    @OnClick(R.id.btn_input)
    protected void onInputButtonClick() {
        final Dialoguer.DoubleDialog doubleDialog = dialoguer.makeDoubleDialog(this, getResources().getString(R.string.enter_number));
        final Context ctx = this;

        doubleDialog.dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    double newNumber = Double.parseDouble(doubleDialog.input.getText().toString());
                    calculator.addNumber(newNumber);
                } catch (NumberFormatException e) {
                    Toast.makeText(ctx, R.string.invalid_number_format, Toast.LENGTH_SHORT).show();
                }
            }
        });
        doubleDialog.dialog.setNegativeButton(android.R.string.cancel, null);
        doubleDialog.dialog.show();
    }

    @OnClick(R.id.btn_delete)
    protected void onDeleteButtonClick() {
        calculator.deleteTopNumber();
    }

    @OnClick(R.id.btn_swap)
    protected void onSwapButtonClick() {
        calculator.swapTopNumbers();
    }

    @OnClick(R.id.btn_clear)
    protected void onClearButtonClick() {
        final AlertDialog.Builder confirmationDialog = dialoguer.makeConfirmationDialog(
                this,
                getResources().getString(R.string.query_clear_stack),
                getResources().getString(R.string.query_sure)
        );

        confirmationDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                calculator.clear();
            }
        });
        confirmationDialog.setNegativeButton(android.R.string.no, null);
        confirmationDialog.show();
    }

    @OnClick(R.id.btn_add)
    protected void onAddButtonClick() {
        calculator.addTopNumbers();
    }

    @OnClick(R.id.btn_subtract)
    protected void onSubtractButtonClick() {
        calculator.subtractTopNumbers();
    }

    @OnClick(R.id.btn_multiply)
    protected void onMultiplyButtonClick() {
        calculator.multiplyTopNumbers();
    }

    @OnClick(R.id.btn_divide)
    protected void onDivideButtonClick() {
        calculator.divideTopNumbers();
    }

    @OnClick(R.id.btn_power)
    protected void onPowerButtonClick() {
        calculator.powerTopNumbers();
    }

    @OnClick(R.id.btn_root)
    protected void onRootButtonClick() {
        calculator.rootTopNumbers();
    }

    @OnClick(R.id.btn_log)
    protected void onLogButtonClick() {
        calculator.logTopNumbers();
    }

    @OnClick(R.id.btn_sin)
    protected void onSinButtonClick() {
        calculator.sinTopNumber();
    }

    @OnClick(R.id.btn_cos)
    protected void onCosButtonClick() {
        calculator.cosTopNumber();
    }

    @OnClick(R.id.btn_tg)
    protected void onTgButtonClick() {
        calculator.tgTopNumber();
    }

    @OnClick(R.id.btn_ctg)
    protected void onCtgButtonClick() {
        calculator.ctgTopNumber();
    }

    private ArrayList<Double> loadNumbersList() {
        ArrayList<Double> result = new ArrayList<>();
        SharedPreferences sprefs = getSharedPreferences(SPREFS_TAG, MODE_PRIVATE);
        String input = sprefs.getString(SPREFS_KEY_VALUES, "");

        if (input != null && !input.isEmpty()) {
            String[] numbers = input.split(";");
            for (String number : numbers) {
                result.add(Double.parseDouble(number));
            }
        }

        return result;
    }

    private void saveNumbersList(List<Double> numbers) {
        SharedPreferences sprefs = getSharedPreferences(SPREFS_TAG, MODE_PRIVATE);
        SharedPreferences.Editor sprefsEditor = sprefs.edit();

        StringBuilder sbuilder = new StringBuilder();
        Iterator<Double> iter = numbers.iterator();
        while (iter.hasNext()) {
            sbuilder.append(iter.next());
            if (iter.hasNext()) {
                sbuilder.append(";");
            }
        }

        sprefsEditor.putString(SPREFS_KEY_VALUES, sbuilder.toString());
        sprefsEditor.commit();
    }
}
