package asak.pro.sms_application;

/**
 * Created by root on 1/22/16.
 */
import org.json.JSONObject;

/**
 * Created by kstanoev on 1/14/2015.
 */
interface AsyncResult
{
    void onResult(JSONObject object);
}