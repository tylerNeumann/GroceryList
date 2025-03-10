package fvtc.edu.grocerylist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RestClient {
    public static final String TAG = "RestClient";
    public static void execGetOneRequest(String url,
                                         Context context,
                                         VolleyCallback volleyCallback)
    {
        Log.d(TAG, "execGetOneRequest: Start");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ArrayList<Item> items = new ArrayList<Item>();
        Log.d(TAG, "execGetOneRequest: " + url);

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);

                            try {
                                JSONObject object = new JSONObject(response);
                                Item item = new Item();
                                item.setId(object.getInt("id"));
                                item.setItem(object.getString("item"));
                                item.setOnShoppingList(RestClientUtil.intToBoolOnList(object));
                                item.setInCart(RestClientUtil.intToBoolInCart(object));
                                item.setOwner(object.getString("owner"));
                                item.setLatitude(object.getDouble("latitude"));
                                item.setLongitude(object.getDouble("longitude"));
                                String jsonPhoto = object.getString("photo");
                                if(jsonPhoto != null)
                                {
                                    byte[] bytePhoto = null;
                                    bytePhoto = Base64.decode(jsonPhoto, Base64.DEFAULT);
                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytePhoto, 0, bytePhoto.length);
                                    item.setPhoto(bmp);
                                }

                                items.add(item);

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            volleyCallback.onSuccess(items);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: error1 = " + error.getMessage());
                        }
                    });

            // Important!!!
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            Log.d(TAG, "execGetOneRequest: Error2 = " + e.getMessage());
        }
    }
    public static void execGetRequest(String url,
                                      Context context,
                                      VolleyCallback volleyCallback)
    {
        Log.d(TAG, "execGetRequest: Start");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ArrayList<Item> items = new ArrayList<Item>();
        Log.d(TAG, "execGetRequest: " + url);

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);

                            try {
                                JSONArray JSONitems = new JSONArray(response);
                                for(int i = 0; i < JSONitems.length(); i++)
                                {
                                    JSONObject object = JSONitems.getJSONObject(i);
                                    Item item = new Item();
                                    item.setId(object.getInt("id"));
                                    item.setItem(object.getString("item"));
                                    item.setOnShoppingList(RestClientUtil.intToBoolOnList(object));
                                    item.setInCart(RestClientUtil.intToBoolInCart(object));
                                    item.setOwner(object.getString("owner"));
                                    item.setLatitude(object.getDouble("latitude"));
                                    item.setLongitude(object.getDouble("longitude"));
                                    String jsonPhoto = object.getString("photo");
                                    if(jsonPhoto != null)
                                    {
                                        byte[] bytePhoto = null;
                                        bytePhoto = Base64.decode(jsonPhoto, Base64.DEFAULT);
                                        Bitmap bmp = BitmapFactory.decodeByteArray(bytePhoto, 0, bytePhoto.length);
                                        item.setPhoto(bmp);
                                    }

                                    items.add(item);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            volleyCallback.onSuccess(items);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: get " + error.getMessage());
                        }
                    });

            // Important!!!
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static void executeRequest(Item item,
                                       String url,
                                       Context context,
                                       VolleyCallback volleyCallback,
                                       int method)
    {
        Log.d(TAG, "executeRequest: " + method + ":" + url);

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject object = new JSONObject();
            object.put("id", item.getId());
            object.put("item", item.getItem());
            object.put("isOnShoppingList", RestClientUtil.boolToIntOnList(item));
            object.put("isInCart", RestClientUtil.boolToIntInCart(item));
            object.put("owner", item.getOwner());
            object.put("latitude", item.getLatitude());
            object.put("longitude", item.getLongitude());
            if(item.getPhoto() != null)
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = Bitmap.createScaledBitmap(item.getPhoto(), 144, 144, false);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String jsonPhoto = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                object.put("photo", jsonPhoto);
            }
            else
            {
                object.put("photo", null);
            }
            final String requestBody = object.toString();
            Log.d(TAG, "executeRequest: " + requestBody);

            JsonObjectRequest request = new JsonObjectRequest(method, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: " + response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: error2 exec = " + error.getMessage());
                    Log.i(TAG, "onErrorResponse: item = " + item);
                    Log.i(TAG, "onErrorResponse: object = " + object);
                    //Log.i(TAG, "onErrorResponse: url = " + url);
                }
            })
            {
                @Override
                public byte[] getBody(){
                    Log.i(TAG, "getBody: " + object.toString());
                    return object.toString().getBytes(StandardCharsets.UTF_8);
                }
            };

            requestQueue.add(request);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void execDeleteRequest(Item item,
                                         String url,
                                         Context context,
                                         VolleyCallback volleyCallback)
    {
        try {
            executeRequest(item, url, context, volleyCallback, Request.Method.DELETE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void execPutRequest(Item item,
                                      String url,
                                      Context context,
                                      VolleyCallback volleyCallback)
    {
        try {
            Log.i(TAG, "execPutRequest: ");
            executeRequest(item, url, context, volleyCallback, Request.Method.PUT);
        } catch (Exception e) {
            Log.i(TAG, "execPutRequest: error");
            throw new RuntimeException(e);
        }
    }
    public static void execPostRequest(Item item,
                                       String url,
                                       Context context,
                                       VolleyCallback volleyCallback)
    {
        try {
            executeRequest(item, url, context, volleyCallback, Request.Method.POST);
        } catch (Exception e) {
            Log.i(TAG, "execPostRequest: ");
            throw new RuntimeException(e);
        }
    }
}
