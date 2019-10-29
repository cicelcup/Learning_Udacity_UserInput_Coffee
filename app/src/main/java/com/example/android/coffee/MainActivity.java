package com.example.android.coffee;

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
import java.util.Locale;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    static int PRICE_OF_COFFEE = 5;
    static int PRICE_OF_CHOCOLATE = 2;
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
        CheckBox whippedCreamBox = findViewById(R.id.hasWhippedCream_checkbox_view);
        boolean hasWhippedCream = whippedCreamBox.isChecked();

        //Figure out if the user wants Whipped Cream
        CheckBox chocolateBox =  findViewById(R.id.hasChocolate_checkbox_view);
        boolean hasChocolate = chocolateBox.isChecked();

        //Put the name in the order
        EditText nameBox = findViewById(R.id.name_editText_view);
        name = nameBox.getText().toString();


        Log.i("Exercise", name); // show the name value in the log


        int priceCalculated = calculatePrice(quantity, PRICE_OF_COFFEE,
                hasWhippedCream, hasChocolate);

        message = createOrderSummary(priceCalculated, hasWhippedCream, hasChocolate, name);

        displayMessage(message);

        Toast.makeText(getApplicationContext(), getString(R.string.orderSummaryReady_text),
                Toast.LENGTH_LONG).show();

        sendEmailButton = findViewById(R.id.sendEmail_Button_View);
        sendEmailButton.setEnabled(true);
    }

    /**
     * Set the price coffee
     *
     * @param numberOfCoffee  quantity of coffee
     * @param price           unit price
     * @param hasWhippedCream Has it Whipped Cream
     * @param hasChocolate     Has it Chocolate
     * @return return total price (coffee * quantity)
     */
    private int calculatePrice(int numberOfCoffee, int price, boolean hasWhippedCream,
                               boolean hasChocolate) {
        int totalPrice = price;

        if (hasWhippedCream) {
            totalPrice += PRICE_OF_WHIPPED_CREAM;
        }

        if (hasChocolate) {
            totalPrice += PRICE_OF_CHOCOLATE;
        }


        return numberOfCoffee * totalPrice;
    }

    /**
     * Create a order summary
     *
     * @param totalPrice is the total price of the order
     * @param hasWhippedCream it says if the order has whipped cream
     * @param hasChocolate it says if the order has chocolate
     * @param name it says the name of the order
     * @return summary string to display on screen
     */

    private String createOrderSummary(int totalPrice, boolean hasWhippedCream,
                                      boolean hasChocolate, String name) {
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

        if(hasChocolate) {
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
     * Increase in one the amount of coffee
     * @param view the text where is indicated the amount of coffee
     */

    public void increment(View view) {
        if (quantity < 10) {
            quantity = quantity + 1;
            display(quantity);
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.No_more_than_ten,
                    Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Reduce in one the amount of coffee
     *
     * @param view the text where is indicated the amount of coffee
     */
    public void decrement(View view) {
        if (quantity > 1) {
            quantity = quantity - 1;
            display(quantity);
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.No_less_than_one,
                    Toast.LENGTH_LONG).show();
        }

    }

    /*function to send the email of the order*/

    public void sendEmail(View view){
//        Array of emails
        String[] emailAddresses = {"cicelcup@gmail.com","xmoyas@gmail.com"};
//        New Intent of sending email
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL,emailAddresses);
        intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.coffeeOrder_text,name));
        intent.putExtra(Intent.EXTRA_TEXT,message);
//        Check if it exists an activity to handle the mail sending

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
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.format(Locale.getDefault(),"%d",number));
    }


    /**
     * This method displays the given price on the screen in USD
     */
    private String displayPrice(int number) {
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        return fmt.format(number);
    }

    /**
     * This method displays the message for the order
     */

    private void displayMessage(String message) {
        TextView orderSummaryTextView = findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }
}