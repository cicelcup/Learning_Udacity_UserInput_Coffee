package com.example.android.coffee;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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

//    Constants to save the Instance
    final static String NAME = "name";
    final static String QUANTITY = "amount";
    final static String HASWHIPPEDCREAM = "cream";
    final static String HASCHOCOLATE= "chocolate";

//    Constants to define the price of the ingredients
    static int PRICE_OF_COFFEE = 5;
    static int PRICE_OF_CHOCOLATE = 2;
    static int PRICE_OF_WHIPPED_CREAM = 1;
    int quantity;
    String message;
    String name;
    boolean hasWhippedCream;
    boolean hasChocolate;
    Button sendEmailButton;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current score
        savedInstanceState.putString(NAME,name);
        savedInstanceState.putInt(QUANTITY,quantity);
        savedInstanceState.putBoolean(HASWHIPPEDCREAM,hasWhippedCream);
        savedInstanceState.putBoolean(HASCHOCOLATE,hasChocolate);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gettingIntanceState(savedInstanceState);

        display(quantity);
        displayMessage(new SpannableStringBuilder(getString(R.string.notOrderYet_Text)));
    }

//    Gettint the state of the current activity
    private void gettingIntanceState(Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            name = savedInstanceState.getString(NAME);
            quantity = savedInstanceState.getInt(QUANTITY);
            hasWhippedCream = savedInstanceState.getBoolean(HASWHIPPEDCREAM);
            hasChocolate = savedInstanceState.getBoolean(HASCHOCOLATE);
        }
        else{
            name = "";
            quantity = 1;
            hasWhippedCream = false;
            hasChocolate = false;
        }
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //Figure out if the user wants Whipped Cream
        CheckBox whippedCreamBox = findViewById(R.id.hasWhippedCream_checkbox_view);
        hasWhippedCream = whippedCreamBox.isChecked();

        //Figure out if the user wants Whipped Cream
        CheckBox chocolateBox =  findViewById(R.id.hasChocolate_checkbox_view);
        hasChocolate = chocolateBox.isChecked();

        //Put the name in the order
        EditText nameBox = findViewById(R.id.name_editText_view);
        name = nameBox.getText().toString();

        Log.i("Exercise", name); // show the name value in the log

        int priceCalculated = calculatePrice(quantity, PRICE_OF_COFFEE,
                hasWhippedCream, hasChocolate);

        SpannableStringBuilder sp = createOrderSummary(priceCalculated,
                hasWhippedCream, hasChocolate, name);

        message = sp.toString();

        displayMessage(sp);

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

    private SpannableStringBuilder createOrderSummary(int totalPrice, boolean hasWhippedCream,
                                      boolean hasChocolate, String name) {
        SpannableStringBuilder sp = new SpannableStringBuilder();

        sp.append(formatText(getString(R.string.orderSummaryName_text)));
        sp.append(name + "\n");
        sp.append(formatText(getString(R.string.quantity_text)));
        sp.append(quantity + "\n");

        if(hasWhippedCream) {
            sp.append(formatText(getString(R.string.hasWhippedCream_text)));
            sp.append(getString(R.string.Yes_text) + "\n");
        }
        else
        {
            sp.append(formatText(getString(R.string.hasWhippedCream_text)));
            sp.append(getString(R.string.No_text) + "\n");
        }

        if(hasChocolate) {
            sp.append(formatText(getString(R.string.hasChocolate_text)));
            sp.append(getString(R.string.Yes_text) + "\n");
        }
        else
        {
            sp.append(formatText(getString(R.string.hasChocolate_text)));
            sp.append(getString(R.string.No_text) + "\n");
        }

        sp.append(formatText(getString(R.string.totalPrice_text)));
        sp.append(displayPrice(totalPrice) + "\n");

        sp.append(formatText(getString(R.string.thanksOrder_Text)));

        return sp;
    }

    private SpannableStringBuilder formatText(String text) {

        //        Variable to format the string in differents ways
        SpannableStringBuilder sp = new SpannableStringBuilder();

//        Variable to bold text
        StyleSpan boldText = new StyleSpan(Typeface.BOLD);

//        Variable to color text
        ForegroundColorSpan colorText = new ForegroundColorSpan(
                getResources().getColor(R.color.colorPrimaryDark));

        sp.append(text);
        int start = 0;
        sp.setSpan(boldText,start,sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(colorText,start,sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
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

    private void displayMessage(SpannableStringBuilder sp) {
        TextView orderSummaryTextView = findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(sp);
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

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.format(Locale.getDefault(),"%d",number));
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
        intent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.orderSummary_text) + ": \n" + message);
//        Check if it exists an activity to handle the mail sending

        if (intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
        sendEmailButton.setEnabled(false);
        displayMessage(new SpannableStringBuilder(getString(R.string.notOrderYet_Text)));
    }

}