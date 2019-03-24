package ru.pukhanov.stackcalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.text.InputType;
import android.widget.EditText;

public class Dialoguer {
    public DoubleDialog makeDoubleDialog(Context ctx, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);

        final EditText doubleInput = new EditText(ctx);
        doubleInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        doubleInput.setRawInputType(Configuration.KEYBOARD_12KEY);
        builder.setView(doubleInput);

        return new DoubleDialog(builder, doubleInput);
    }

    public AlertDialog.Builder makeConfirmationDialog(Context ctx, String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(text);
        return builder;
    }

    public class DoubleDialog {
        public AlertDialog.Builder dialog;
        public EditText input;

        public DoubleDialog(AlertDialog.Builder dialog, EditText input) {
            this.dialog = dialog;
            this.input = input;
        }
    }
}
