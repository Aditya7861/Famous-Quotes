package quotes.first.aditya.famousquotes;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView Author;
    private TextView Quote;

    RequestQueue requestQueue;



    String JSONurl = "http://api.forismatic.com/api/1.0/?method=getQuote&key=457653&format=json&lang=en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fetch_data();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void fetch_data()
    {
        requestQueue = Volley.newRequestQueue(this);
        Typeface mycustomfont=Typeface.createFromAsset(getAssets(),"Fonts/DancingScript-Bold.ttf");
        // Casts results into the TextView found within the main layout XML with id jsonData
        Author = (TextView) findViewById(R.id.Author);
        Author.setTypeface(mycustomfont);
        Quote=(TextView)findViewById(R.id.Quote);
        Quote.setTypeface(mycustomfont);

        // Creating the JsonObjectRequest class called obreq, passing required parameters:
        //GET is used to fetch data from the server, JsonURL is the URL to be fetched from.
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, JSONurl,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            // Retrieves the string labeled "colorName" and "description" from
                            //the response JSON Object
                            //and converts them into javascript objects
                            String quote_info = response.getString("quoteText");
                            String author_info = response.getString("quoteAuthor");

                            // Adds strings from object to the "data" string
                            //   data += "Color Name: " + Author +
                            //       "nDescription : " + desc;

                            Quote.setText(quote_info);
                            Author.setText(author_info);

                            Log.v("data",quote_info);

                            // Adds the data string to the TextView "results"

                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        // Adds the JSON object request "obreq" to the request queue
        requestQueue.add(obreq);

        //On click listiner
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent sharing_content=new Intent(Intent.ACTION_SEND);
                sharing_content.setType("text/plain");
                sharing_content.putExtra(Intent.EXTRA_SUBJECT,"Subject here");
                sharing_content.putExtra(Intent.EXTRA_TEXT,Quote.getText().toString()+"\n\n\n\n"+ " "+Author.getText().toString());
                startActivity(sharing_content);

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {

            Intent intent = new Intent(getApplicationContext(),About.class);
            startActivity(intent);

        } else if (id == R.id.nav_shop) {


        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Famous Quotes");
            intent.setData(Uri.parse("mailto:aditya9640@gmail.com")); // or just "mailto:" for blank
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
            startActivity(intent);

        } else if (id == R.id.thumbs_up) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/xda.aditya"));
            startActivity(browserIntent);

        } else if (id == R.id.share_it) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void new_quotes(View view) {
        fetch_data();
    }
}
