package fvtc.edu.grocerylist;

import org.json.JSONException;
import org.json.JSONObject;
//ternary operator syntax variable = (condition) ? expressionTrue : expressionFalse;
public class RestClientUtil {
   static boolean bool;
   static int i;
    public static boolean intToBoolOnList(JSONObject object) throws JSONException {
        return bool = (object.getInt("isOnShoppingList") == 1) ?  true : false;
    }
    public static boolean intToBoolInCart(JSONObject object) throws JSONException {
        return bool = (object.getInt("isInCart") == 1) ? true : false;
    }
    public static int boolToIntOnList(Item item) {
        return i = (item.getIsOnShoppingList()) ? 1 : 0;
    }
    public static int boolToIntInCart(Item item) {
        return i = (item.getIsInCart()) ? 1 : 0;
    }
}
