package com.example.android.coffee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
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
    final static String HAS_WHIPPED_CREAM = "cream";
    final static String HAS_CHOCOLATE = "chocolate";

//    Variable to define the price of the ingredients and the email
    int coffee_price;
    int chocolate_price;
    int whipped_cream_price;

//    variables of the current values input for the user
    int quantity;
    String message;
    String name;
    boolean hasWhippedCream;
    boolean hasChocolate;

//Views variables
    Button sendEmailButton;
    EditText nameBox;
    CheckBox chocolateBox;
    CheckBox whippedCreamBox;

//    Create the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        //Menu main is a layout created
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //open the menu selected

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Check if setting was pressed
        if (id == R.id.actions_settings) {
            //Open a new activity for setting the configurations
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            //open the settings activity
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        setOrderVariables();
        // Save the user's current edition
        savedInstanceState.putString(NAME,name);
        savedInstanceState.putInt(QUANTITY,quantity);
        savedInstanceState.putBoolean(HAS_WHIPPED_CREAM,hasWhippedCream);
        savedInstanceState.putBoolean(HAS_CHOCOLATE,hasChocolate);

        super.onSaveInstanceState(savedInstanceState);
    }

    //    Getting the state of the current activity
    private void gettingInstanceState(Bundle savedInstanceState) {
//        if the state is different than null, filled the views
        if (savedInstanceState!=null){
            nameBox.setText(savedInstanceState.getString(NAME));
            quantity = savedInstanceState.getInt(QUANTITY);
            whippedCreamBox.setChecked(savedInstanceState.getBoolean(HAS_WHIPPED_CREAM));
            chocolateBox.setChecked(savedInstanceState.getBoolean(HAS_CHOCOLATE));
        }
//        set the default values of the views
        else{
            nameBox.setText("");
            quantity = 1;
            whippedCreamBox.setChecked(false);
            chocolateBox.setChecked(false);
        }
    }

//    in on resume is getting the preferences of the activity (on resume comes after on create)
    @Override
    protected void onResume() {
        super.onResume();
        gettingPreferences();
    }

//    on create of the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initiate of the views
        whippedCreamBox = findViewById(R.id.hasWhippedCream_checkbox_view);
        chocolateBox =  findViewById(R.id.hasChocolate_checkbox_view);
        nameBox = findViewById(R.id.name_editText_view);

//        getting the instance state
        gettingInstanceState(savedInstanceState);

        display(quantity);
        displayMessage(new SpannableStringBuilder(getString(R.string.notOrderYet_Text)));
    }

//    function to getting the preferences
    private void gettingPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(this);

//        coffee price
        String stringPreferenceValue;
        stringPreferenceValue = sharedPreferences.getString(
                getString(R.string.coffee_price_key),
                getString(R.string.coffee_price_default));

        assert stringPreferenceValue != null;
        coffee_price = Integer.parseInt(stringPreferenceValue);

//        whipped cream price
        stringPreferenceValue = sharedPreferences.getString(
                getString(R.string.whipped_cream_price_key),
                getString(R.string.whipped_cream_price_default));

        assert stringPreferenceValue != null;
        whipped_cream_price = Integer.parseInt(stringPreferenceValue);

//        chocolate price
        stringPreferenceValue = sharedPreferences.getString(
                getString(R.string.chocolate_price_key),
                getString(R.string.chocolate_price_default));
        assert stringPreferenceValue != null;
        chocolate_price = Integer.parseInt(stringPreferenceValue);

//        email
        String o = sharedPreferences.getString(
                getString(R.string.email_key),
                getString(R.string.email_default));
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
// ser order variables according user option
        setOrderVariables();

//        calculated the price
        int priceCalculated = calculatePrice(quantity, coffee_price,
                hasWhippedCream, hasChocolate);

//        Create order summary
        SpannableStringBuilder sp = createOrderSummary(priceCalculated,
                hasWhippedCream, hasChocolate, name);

//      Get the messages to be used in the email
        message = sp.toString();

//        Display the message with different format
        displayMessage(sp);

        Toast.makeText(getApplicationContext(), getString(R.string.orderSummaryReady_text),
                Toast.LENGTH_LONG).show();

        sendEmailButton = findViewById(R.id.sendEmail_Button_View);
        sendEmailButton.setEnabled(true);
    }

    private void setOrderVariables() {
        //Figure out if the user wants Whipped Cream
        hasWhippedCream = whippedCreamBox.isChecked();

        //Figure out if the user wants Whipped Cream
        hasChocolate = chocolateBox.isChecked();

        //Put the name in the order
        name = nameBox.getText().toString();
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
            totalPrice += whipped_cream_price;
        }

        if (hasChocolate) {
            totalPrice += chocolate_price;
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
//  it uses a Spannable to provided different color format
        SpannableStringBuilder sp = new SpannableStringBuilder();

        sp.append(formatText(getString(R.string.orderSummaryName_text),true));
        sp.append(name).append("\n");
        sp.append(formatText(getString(R.string.quantity_text),true));
        sp.append(String.valueOf(quantity)).append("\n");

        if(hasWhippedCream) {
            sp.append(formatText(getString(R.string.hasWhippedCream_text),true));
            sp.append(getString(R.string.Yes_text)).append("\n");
        }
        else
        {
            sp.append(formatText(getString(R.string.hasWhippedCream_text),true));
            sp.append(getString(R.string.No_text)).append("\n");
        }

        if(hasChocolate) {
            sp.append(formatText(getString(R.string.hasChocolate_text),true));
            sp.append(getString(R.string.Yes_text)).append("\n");
        }
        else
        {
            sp.append(formatText(getString(R.string.hasChocolate_text),true));
            sp.append(getString(R.string.No_text)).append("\n");
        }

        sp.append(formatText(getString(R.string.totalPrice_text),true));
        sp.append(displayPrice(totalPrice)).append("\n");

        sp.append(formatText(getString(R.string.thanksOrder_Text),false));

        return sp;
    }
//  function to format the summary order
    private SpannableStringBuilder formatText(String text,boolean optionCenter) {

        //        Variable to format the string in different ways
        SpannableStringBuilder sp = new SpannableStringBuilder();

//        Variable to bold text
        StyleSpan boldText = new StyleSpan(Typeface.ITALIC);

//        Variable to color text
        ForegroundColorSpan colorText = new ForegroundColorSpan(
                getResources().getColor(R.color.colorPrimary));


        sp.append(text);
        int start = 0;
//        bold format
        sp.setSpan(boldText,start,sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        color format
        sp.setSpan(colorText,start,sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        bullet option
        if (optionCenter) {
            BulletSpan bulletText = new BulletSpan(
                    16,getResources().getColor(R.color.colorPrimaryDark));
            sp.setSpan(bulletText, start, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else{
            AlignmentSpan t = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
            sp.setSpan(t, start, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

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