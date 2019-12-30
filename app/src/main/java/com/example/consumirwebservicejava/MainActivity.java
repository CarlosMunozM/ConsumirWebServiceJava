package com.example.consumirwebservicejava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText txtFrase;
    TextView lbRespuesta;

    String frase = "", analisis = "";

    private RequestQueue rq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rq = Volley.newRequestQueue(getApplicationContext());

        txtFrase = (EditText) findViewById(R.id.txtFrase);
        lbRespuesta = (TextView) findViewById(R.id.lbRespuesta);

    }


    public void consumir(View view)
    {
        //Toast.makeText(getApplicationContext(), "se ha dado clic", Toast.LENGTH_LONG).show();
        realizarAnalisis();
    }

    public void realizarAnalisis() {
        StringRequest str = new StringRequest(Request.Method.POST,
                "http://192.168.100.232:8080/WS_AnalisisSentimientos/wsAnalisisSentimientos",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try
                        {
                            analisis = parsear(response);
                            if(analisis.equals("P+"))
                                lbRespuesta.setText("Muy Positivo");
                            else if(analisis.equals("P"))
                                lbRespuesta.setText("Positivo");
                            else if(analisis.equals("NEU"))
                                lbRespuesta.setText("Neutral");
                            else if(analisis.equals("N"))
                                lbRespuesta.setText("Negativo");
                            else if(analisis.equals("N+"))
                                lbRespuesta.setText("Muy Negativo");
                            else if(analisis.equals("NONE"))
                                lbRespuesta.setText("Ningún sentimiento detectado");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error->", error.toString());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("frase",txtFrase.getText().toString());


                return parametros;
            }
        };

        rq.add(str);

    }

    private String parsear(String cadena) throws JSONException {

        JSONObject jsonObject = new JSONObject(cadena);
        return jsonObject.getString("score_tag");
    }
}
