package com.example.android.coffeerestaurant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    static int PRICE_OF_COFFEE = 5;
    static int PRICE_OF_CHOCOLAT = 2;
    static int PRICE_OF_WHIPPED_CREAM = 1;
    int quantity = 1;
    String message;
    String name;
    Button sendEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display(quantity);
        displayMessage(getString(R.string.notOrderYet_Text));
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //Figure out if the user wants Whipped Cream
        CheckBox whippedCreamBox = (CheckBox) findViewById(R.id.hasWhippeCream_checkbox_view);
        boolean hasWhippedCream = whippedCreamBox.isChecked();

        //Figure out if the user wants Whipped Cream
        CheckBox chocolatBox = (CheckBox) findViewById(R.id.hasChocolat_checkbox_view);
        boolean hasChocolat = chocolatBox.isChecked();

        //Put the name in the order
        EditText nameBox = (EditText) findViewById(R.id.name_editText_view);
        name = nameBox.getText().toString();


        Log.i("Ejercicio", name); // show the name value in the log


        message = createOrderSummary(calculatePrice(quantity, PRICE_OF_COFFEE, hasWhippedCream, hasChocolat), hasWhippedCream, hasChocolat, name);

        displayMessage(message);

        Toast.makeText(getApplicationContext(), getString(R.string.orderSummaryReady_text), Toast.LENGTH_LONG).show();

        sendEmailButton = (Button)findViewById(R.id.sendEmail_Button_View);
        sendEmailButton.setEnabled(true);
    }

    /**
     * Calcula el precio del cafe
     *
     * @param numberOfCoffee  cantidad de cafes
     * @param price           precio unitario
     * @param hasWhippedCream Tiene crema batida
     * @param hasChocolat     tiene chocolate
     * @return regresa el precio total (cafes * cantidad)
     */
    private int calculatePrice(int numberOfCoffee, int price, boolean hasWhippedCream, boolean hasChocolat) {
        int totalprice = price;

        if (hasWhippedCream) {
            totalprice += PRICE_OF_WHIPPED_CREAM;
        }

        if (hasChocolat) {
            totalprice += PRICE_OF_CHOCOLAT;
        }


        return numberOfCoffee * totalprice;
    }

    /**
     * Crea un sumario de la orden
     *
     * @param totalPrice el precio total de la orden
     * @param hasWhippedCream Indica si tiene crema batida o no
     * @param hasChocolat Insdica si tiene chocolate o no
     * @param name
     * @return
     */

    private String createOrderSummary(int totalPrice, boolean hasWhippedCream, boolean hasChocolat, String name) {
        String summary;

        summary = getString(R.string.orderSummary_text) + "\n";
        summary += getString(R.string.orderSummaryName_text,name)+ "\n";
        summary += getString(R.string.quantity_text) +": " + quantity + "\n";
        if(hasWhippedCream) {
            summary += getString(R.string.hasWhippedCream_text) + ": " + getString(R.string.Yes_text) + "\n";
        }
        else
        {
            summary += getString(R.string.hasWhippedCream_text) + ": " + getString(R.string.No_text) + "\n";
        }

        if(hasChocolat) {
            summary += getString(R.string.hasChocolate_text) + ": " + getString(R.string.Yes_text) + "\n";
        }
        else
        {
            summary += getString(R.string.hasChocolate_text) + ": " + getString(R.string.No_text) + "\n";
        }


        summary += getString(R.string.totalPrice_text) + ": " + displayPrice(totalPrice) + "\n";
        summary += getString(R.string.thanksOrder_Text);
        return summary;
    }

    /**
     * @param view Incrementa en uno la cantidad de cafes
     */

    public void increment(View view) {
        if (quantity < 10) {
            quantity = quantity + 1;
            display(quantity);
        }
        else{
            Toast.makeText(getApplicationContext(), "You cannot add more than 10 coffees", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Reduce en 1 la cantidad de cafes
     *
     * @param view
     */
    public void decrement(View view) {
        if (quantity > 1) {
            quantity = quantity - 1;
            display(quantity);
        }
        else{
            Toast.makeText(getApplicationContext(), "You cannot have less than 1 coffee", Toast.LENGTH_LONG).show();
        }

    }

    public void sendEmail(View view){
        String[] emailAddresses = {"cicelcup@gmail.com","xmoyas@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL,emailAddresses);
        intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.coffeeOrder_text,name));
        intent.putExtra(Intent.EXTRA_TEXT,message);
        if (intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
        sendEmailButton.setEnabled(false);
        displayMessage(getString(R.string.notOrderYet_Text));
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }


    /**
     * This method displays the given price on the screen.
     */
    private String displayPrice(int number) {
        return NumberFormat.getCurrencyInstance().format(number);
    }

    /**
     * This method displays the message for the order
     */

    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }
}