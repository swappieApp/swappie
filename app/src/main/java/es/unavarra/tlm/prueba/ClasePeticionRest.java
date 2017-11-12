package es.unavarra.tlm.prueba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import es.unavarra.tlm.prueba.PantallaPrincipal.AdaptadorProductos;
import es.unavarra.tlm.prueba.model.Producto2;
import es.unavarra.tlm.prueba.PantallaPrincipal.SwipeStackCardListener;
import es.unavarra.tlm.prueba.PantallaPrincipal.UsuarioRegistrado;
import es.unavarra.tlm.prueba.model.Objeto;
import link.fls.swipestack.SwipeStack;

/**
 * Created by ibai on 10/20/17.
 */

public class ClasePeticionRest {

    public ClasePeticionRest(){
        super();
    }

    public static ArrayList<KeyValue> peticionRest(final ArrayList<KeyValue> parametros, final String funcionAPI, final String metodo){

        final ArrayList<KeyValue> respuesta = new ArrayList<>();

        try {

            respuesta.clear();

            Log.d("etiqueta", "size_params = "+parametros.size());

            String urlParametros = "";
            if (parametros != null) {
                if (parametros.size() != 0) {
                    urlParametros = URLEncoder.encode(parametros.get(0).getKey(), "utf-8") + "=" + URLEncoder.encode(parametros.get(0).getValue(), "utf-8");
                    for (int x = 1; x < parametros.size(); x++) {
                        urlParametros += "&" + URLEncoder.encode(parametros.get(x).getKey(), "utf-8") + "=" + URLEncoder.encode(parametros.get(x).getValue(), "utf-8");
                    }
                }
            }

            String stringURL = "https://aux.swappie.tk/api/" + funcionAPI + ".php?"+urlParametros;
            URL url = new URL(stringURL);

            Log.d("etiqueta", String.valueOf(url));

            final HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
            myConnection.setInstanceFollowRedirects(false);

            if (metodo.equals("post")){
                myConnection.setDoOutput(true);
            }

            if (myConnection.getResponseCode() == 200){

                Log.d("etiqueta", "entro al if");
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                jsonReader.setLenient(true);
                jsonReader.beginObject(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    String value = jsonReader.nextString();

                    respuesta.add(new KeyValue(key, value));
                }
                jsonReader.close();

            } else {
                Log.d("etiqueta", "entro al else");
                respuesta.add(new KeyValue("ok", "false"));
                respuesta.add(new KeyValue("error", "error en la peticion"));
            }

            myConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("etiqueta", "SALGO");
        return respuesta;

    }

    private static ArrayList<KeyValue> doFileUpload(File foto) {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String urlString = "https://aux.swappie.tk/api/guardar_foto.php";

        try {

            Log.d("etiqueta", "URI:"+foto.getAbsolutePath());
            //------------------ CLIENT REQUEST
            FileInputStream fileInputStream = new FileInputStream(foto);
            // open a URL connection to the Servlet
            URL url = new URL(urlString);
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + foto.getName() + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            Log.e("Debug", "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        } catch (IOException ioe) {
            Log.e("Debug", "error: " + ioe.getMessage(), ioe);
        }

        //------------------ read the SERVER RESPONSE
        try {

            inStream = new DataInputStream(conn.getInputStream());
            String str;

            ArrayList<KeyValue> respuesta = new ArrayList<>();
            InputStreamReader responseBodyReader = new InputStreamReader(inStream, "UTF-8");
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            jsonReader.setLenient(true);
            jsonReader.beginObject(); // Start processi ng the JSON object
            while (jsonReader.hasNext()) { // Loop through all keys
                String key = jsonReader.nextName(); // Fetch the next key
                String value = jsonReader.nextString();
                respuesta.add(new KeyValue(key, value));
            }
            jsonReader.close();

            /*while ((str = inStream.readLine()) != null) {

                Log.e("Debug", "Server Response " + str);

            }*/

            inStream.close();

            return respuesta;

        } catch (IOException ioex) {
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }
        return null;
    }

    public static class GuardarUsuario extends AsyncTask<String, String, Integer> {

        String funcionAPI = "guardar_usuario";
        String nombre, apellidos, email, metodoLogin;
        ProgressDialog dialog;

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public GuardarUsuario(Context context, String nombre, String apellidos, String email, String password, String ubicacion, String metodoLogin) {

            this.context = context;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.email = email;
            this.metodoLogin = metodoLogin;

            parametros.add(new KeyValue("nombre", nombre));
            parametros.add(new KeyValue("apellidos", apellidos));
            parametros.add(new KeyValue("metodo_login", metodoLogin));
            parametros.add(new KeyValue("email", email));
            if (password != null) {
                parametros.add(new KeyValue("password", password));
            }
            parametros.add(new KeyValue("ubicacion", ubicacion));
            this.dialog = new ProgressDialog(context);
            this.dialog.setMessage("Please wait");
            this.dialog.show();

        }


        @Override
        protected Integer doInBackground(String... strings) {

            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");

            int idUsuario = 0;

            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                if (respuesta.get(1).getKey().equals("id_usuario")){
                    idUsuario = Integer.parseInt(respuesta.get(1).getValue());
                }
            }
            return idUsuario;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            guardarUsuarioEnSharedPreferences(context, result, metodoLogin, nombre, apellidos, email);
            if (result==0){
                this.dialog.dismiss();
                mostrarToast((Activity)context, "Email ya registrado");

                //Boramos las SharedPreferences
                SharedPreferences info = context.getSharedPreferences("Config", 0);
                SharedPreferences.Editor editor = info.edit();
                editor.clear();
                // Se borra la sesión de Shared Preferences.
                editor.putBoolean("sesion", false);
                editor.commit();

            }else{
                mostrarToast((Activity)context, "Creado usuario Nº "+result);
                Intent intent = new Intent(context, UsuarioRegistrado.class);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        }

    }

    public static class GuardarSwipe extends AsyncTask<String, String, String> {

        String funcionAPI = "guardar_swipe";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Activity activity;

        public GuardarSwipe(Activity activity, int idUsuario, int idObjeto, boolean decision) {
            parametros.add(new KeyValue("id_usuario1", idUsuario+""));
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            parametros.add(new KeyValue("decision", decision+""));
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (Integer.parseInt(parametros.get(0).getValue()) == 0){
                return "";
            }
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                return "true";
            }else if (respuesta.get(1).getKey().equals("error")){
                return respuesta.get(1).getValue();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("true")){
                mostrarToast(activity, "Swipe guardado correctamente");
            }else if (result.equals("")){
                mostrarToast(activity, "Error al guardar el swipe");
            }
        }

    }

    public static class GuardarMatch extends AsyncTask<String, String, String> {

        String funcionAPI = "guardar_match";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public GuardarMatch(Context context, int idUsuario1, int idUsuario2, boolean chatEmpezado) {
            parametros.add(new KeyValue("id_usuario1", idUsuario1+""));
            parametros.add(new KeyValue("id_usuario2", idUsuario2+""));
            parametros.add(new KeyValue("chat_empezado", chatEmpezado+""));
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                return "true";
            }else if (respuesta.get(1).getKey().equals("error")){
                return respuesta.get(1).getValue();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("true")){
                mostrarToast((Activity)context, "Match guardado correctamente");
            }else{
                mostrarToast((Activity)context, "Error al guardar el match");
            }
        }

    }

    public static class CrearChat extends AsyncTask<String, String, String> {

        String funcionAPI = "crear_chat";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public CrearChat(Context context, int idUsuario1, int idUsuario2) {
            parametros.add(new KeyValue("id_usuario1", idUsuario1+""));
            parametros.add(new KeyValue("id_usuario2", idUsuario2+""));
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                return "true";
            }else if (respuesta.get(1).getKey().equals("error")){
                return respuesta.get(1).getValue();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("true")){
                mostrarToast((Activity)context, "Chat creado correctamente");
            }else{
                mostrarToast((Activity)context, "Error al crear el chat");
            }
        }

    }

    public static class GuardarMensaje extends AsyncTask<String, String, String> {

        String funcionAPI = "guardar_mensaje";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public GuardarMensaje(Context context, int idChat, int idAutor, String mensaje) {
            parametros.add(new KeyValue("id_chat", idChat+""));
            parametros.add(new KeyValue("id_autor", idAutor+""));
            parametros.add(new KeyValue("mensaje", mensaje));
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                return "true";
            }else if (respuesta.get(1).getKey().equals("error")){
                return respuesta.get(1).getValue();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("true")){
                mostrarToast((Activity)context, "Mensaje guardado correctamente");
            }else{
                mostrarToast((Activity)context, "Error al guardar el mensaje");
            }
        }

    }

    public static class GuardarObjeto extends AsyncTask<String, String, String> {

        String funcionAPI = "guardar_objeto";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        File foto;
        Context context;
        Activity activity;

        public GuardarObjeto(Activity activity, int idUsuario, String descripcion, File foto) {
            parametros.add(new KeyValue("id_usuario", idUsuario+""));
            parametros.add(new KeyValue("descripcion", descripcion));
            this.foto = foto;
            this.context = activity;
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... strings) {

            int idObjeto = guardarFoto(activity, foto);
            Log.e("etiqueta", "IDFoto = "+idObjeto);
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
                return "true";
            }else if (respuesta.get(1).getKey().equals("error")){
                return respuesta.get(1).getValue();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("true")){
                mostrarToast(activity, "Objeto guardado correctamente");
                Intent intent = new Intent(activity, UsuarioRegistrado.class);
                context.startActivity(intent);
                activity.finish();
            }else{
                mostrarToast(activity, "Error al guardar el objeto");
            }
        }

    }

    public static int guardarFoto(Activity activity, File foto) {

        //String funcionAPI = "guardar_objeto";
        //ArrayList<KeyValue> parametros = new ArrayList<>();

        ArrayList<KeyValue> respuesta = doFileUpload(foto);
        int idObjeto = 0;
        if (respuesta.get(0).getKey().equals("ok") && respuesta.get(0).getValue().equals("true")){
            if (respuesta.get(1).getKey().equals("id_objeto")){
                idObjeto = Integer.parseInt(respuesta.get(1).getValue());
            }
        }


        if (idObjeto != 0){
            //mostrarToast(activity, "Foto del Objeto Nº "+ idObjeto +" guardada correctamente");
        }else{
            //mostrarToast(activity, "Error al crear el objeto");
        }

        return idObjeto;

    }

    public static class ComprobarSwipe extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "comprobar_swipe";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public ComprobarSwipe(Context context, int idUsuario1, int idObjeto) {
            parametros.add(new KeyValue("id_usuario1", idUsuario1+""));
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            this.context = context;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                if (result.get(1).getValue().equals("true")){
                    mostrarToast((Activity)context, "MATCH!");
                }else if (result.get(1).getValue().equals("false")){
                    mostrarToast((Activity)context, "NO MATCH!");
                }
            }else if (result.get(1).getKey().equals("error")){
                mostrarToast((Activity)context, "ERROR: " + result.get(1).getValue());
            }

            if (result.equals("true")){
                mostrarToast((Activity)context, "Objeto guardado correctamente");
            }else{
                mostrarToast((Activity)context, "Error al guardar el objeto");
            }
        }

    }

    public static class CogerSwipes extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "coger_swipes";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public CogerSwipes(Context context, int idUsuario1) {
            parametros.add(new KeyValue("id_usuario1", idUsuario1+""));
            this.context = context;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                mostrarToast((Activity)context, "\"JSON: \" + result.get(1).getValue()");
            }else if (result.get(1).getKey().equals("error")){
                mostrarToast((Activity)context, "\"JSON: \" + result.get(1).getValue()");
            }

        }

    }

    public static class CogerInfoObjeto extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "coger_info_objeto";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public CogerInfoObjeto(Context context, int idObjeto) {
            parametros.add(new KeyValue("id_objeto", idObjeto+""));
            this.context = context;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                mostrarToast((Activity)context, "JSON: " + result.get(1).getValue());
            }else if (result.get(1).getKey().equals("error")){
                mostrarToast((Activity)context, "JSON: " + result.get(1).getValue());
            }

        }

    }

    public static class HacerLogin extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "hacer_login";
        String nombre, apellidos, email, metodoLogin;
        ProgressDialog dialog;

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public HacerLogin(Context context, String metodoLogin, String email, String password) {

            parametros.add(new KeyValue("metodo_login", metodoLogin));
            parametros.add(new KeyValue("email", email));
            parametros.add(new KeyValue("password", password));

            this.context = context;
            this.email=email;
            this.metodoLogin="email";

            this.dialog = new ProgressDialog(context);
            this.dialog.setMessage("Please wait");
            this.dialog.show();

        }

        public HacerLogin(Context context, String metodoLogin, String email) {
            ArrayList<KeyValue> parametros = new ArrayList<>();
            parametros.add(new KeyValue("metodo_login", metodoLogin));
            parametros.add(new KeyValue("email", email));
            this.context = context;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.size()==0){
                Log.d("etiqueta","nulo1");
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);

            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){

                int idUsuario = Integer.parseInt(result.get(1).getValue());
                this.nombre = result.get(2).getValue();
                this.apellidos = result.get(3).getValue();

                mostrarToast((Activity)context, "Logueado usuario Nº " + result.get(1).getValue());

                guardarUsuarioEnSharedPreferences(context, Integer.parseInt(result.get(1).getValue()), metodoLogin, nombre, apellidos, email);
                Intent intent = new Intent(context, UsuarioRegistrado.class);
                context.startActivity(intent);
                ((Activity)context).finish();

            }else if (result.get(1).getKey().equals("error")){
                this.dialog.dismiss();
                mostrarToast((Activity)context, "ERROR: " + result.get(1).getValue());
            }
        }

    }

    public static class ActualizarChat extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "actualizar_chat";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public ActualizarChat(Context context, int idUsuario, int idChat) {
            parametros.add(new KeyValue("id_usuario", idUsuario + ""));
            parametros.add(new KeyValue("id_chat", idChat + ""));
            this.context = context;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                mostrarToast((Activity)context, "JSON: " + result.get(1).getValue());
            }else if (result.get(1).getKey().equals("error")){
                mostrarToast((Activity)context, "JSON: " + result.get(1).getValue());
            }

        }

    }

    public static class CogerObjetosInicio extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "coger_objetos_inicio";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;
        Activity activity;

        public CogerObjetosInicio(Context context, String idUsuario) {
            parametros.add(new KeyValue("id_usuario", idUsuario));
            this.context = context;
            this.activity = (Activity)context;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                mostrarToast((Activity)context, "JSON: " + result.get(1).getValue());

                if (!result.get(1).getValue().equals("[]")){
                    Gson gson = new Gson();
                    Log.e("etiqueta", result.get(1).getValue());
                    Objeto[] objetos = gson.fromJson(result.get(1).getValue(), Objeto[].class);
                    new CargarDatos(objetos, activity).executeOnExecutor(THREAD_POOL_EXECUTOR);
                }else{
                    Log.e("etiqueta", "No hay objetos");
                }

            }else if (result.get(1).getKey().equals("error")){
                mostrarToast((Activity)context, "JSON: " + result.get(1).getValue());
            }

        }

    }

    public static class CogerObjetosAleatoriosInicio extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "coger_objetos_aleatorios";

        static ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;
        Activity activity;

        public CogerObjetosAleatoriosInicio(Context context) {
            this.context = context;
            this.activity = (Activity)context;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);
            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){
                //mostrarToast((Activity)context, "JSON: " + result.get(1).getValue());

                Gson gson = new Gson();
                Objeto[] objetos = gson.fromJson(result.get(1).getValue(), Objeto[].class);
                new CargarDatos(objetos, activity).executeOnExecutor(THREAD_POOL_EXECUTOR);

            }else if (result.get(1).getKey().equals("error")){
                mostrarToast((Activity)context, "JSON: " + result.get(1).getValue());
            }

        }

    }

    public static class ComprobarFacebook extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "comprobar_facebook";
        String nombre, apellidos, email, metodoLogin,password,ubicacion;

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;

        public ComprobarFacebook(Context context, String email) {
            parametros.add(new KeyValue("email", email));
            this.context = context;
            this.email=email;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            if (respuesta.size()==0){
                Log.d("etiquetafb","nulo1");
            }

            for (int i=0;i<respuesta.size();i++){
                Log.d("etiquetafb",respuesta.get(i).getKey());
                Log.d("etiquetafb",respuesta.get(i).getValue());
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);

            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){

                Log.d("etiquetafb","entro al if");

                int idUsuario = Integer.parseInt(result.get(1).getValue());

                this.guardarUsuarioEnSharedPreferences(Integer.parseInt(result.get(1).getValue()));

                mostrarToast((Activity)context, "Logueado usuario Nº " + result.get(1).getValue());


            }else if (result.get(1).getKey().equals("error")){

                Log.d("etiquetafb","entro al else");

                if (result.get(1).getValue().equals("no registrado")){

                    Log.d("etiquetafb","entro a no registrado");


                    SharedPreferences settings = context.getSharedPreferences("Config", 0);
                    String name=settings.getString("nombre","");
                    String apellidos= settings.getString("apellidos","");
                    this.nombre=name;
                    this.apellidos=apellidos;
                    this.password="";
                    this.ubicacion=GetLocation.getCoords((Activity)context);
                    this.metodoLogin="facebook";

                    new ClasePeticionRest.GuardarUsuario(this.context,this.nombre,this.apellidos,this.email,this.password,this.ubicacion,this.metodoLogin).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }
                mostrarToast((Activity)context, "ERROR: " + result.get(1).getValue());
            }
        }

        public void guardarUsuarioEnSharedPreferences(int id){
                Intent intent = new Intent(context, UsuarioRegistrado.class);
                   context.startActivity(intent);
                ((Activity)context).finish();

        }

    }

    public static class ComprobarGoogle extends AsyncTask<String, String, ArrayList<KeyValue>> {

        String funcionAPI = "comprobar_google";
        String nombre, apellidos, email, metodoLogin,password,ubicacion;

        ArrayList<KeyValue> parametros = new ArrayList<>();
        Context context;


        public ComprobarGoogle(Context context, String nombre, String apellidos, String email, String ubicacion) {

            parametros.add(new KeyValue("email", email));
            this.context = context;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.ubicacion = ubicacion;
            this.email=email;
        }

        @Override
        protected ArrayList<KeyValue> doInBackground(String... strings) {
            ArrayList<KeyValue> respuesta = peticionRest(parametros, funcionAPI, "get");
            return respuesta;
        }

        @Override
        protected void onPostExecute(ArrayList<KeyValue> result) {
            super.onPostExecute(result);

            if (result.get(0).getKey().equals("ok") && result.get(0).getValue().equals("true")){

                int idUsuario = Integer.parseInt(result.get(1).getValue());

                this.guardarUsuarioEnSharedPreferences(Integer.parseInt(result.get(1).getValue()));
                mostrarToast((Activity)context, "Logueado usuario Nº " + result.get(1).getValue());


            }else if (result.get(1).getKey().equals("error")){
                if (result.get(1).getValue().equals("no registrado")){

                    SharedPreferences settings = context.getSharedPreferences("Config", 0);
                    String name=settings.getString("nombre","");
                    String apellidos= settings.getString("apellidos","");
                    this.nombre=name;
                    this.apellidos=apellidos;
                    this.password="";
                    this.ubicacion=GetLocation.getCoords((Activity)context);
                    this.metodoLogin="google";

                    new ClasePeticionRest.GuardarUsuario(this.context,this.nombre,this.apellidos,this.email,this.password,this.ubicacion,this.metodoLogin).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                }else{
                    mostrarToast((Activity)context, "ERROR: " + result.get(1).getValue());
                }
            }
        }

        public void guardarUsuarioEnSharedPreferences(int id){

            Intent intent = new Intent(context, UsuarioRegistrado.class);
            context.startActivity(intent);
            ((Activity)context).finish();
        }


    }

    public static class KeyValue{

        String key, value;

        public KeyValue(String key, String value){
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static void mostrarToast(final Activity activity, final String frase){
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(activity, frase, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void guardarUsuarioEnSharedPreferences(Context context, int id, String metodoLogin, String nombre, String apellidos, String email){

        SharedPreferences settings = context.getSharedPreferences("Config", 0);
        SharedPreferences.Editor editor = settings.edit();

        Log.e("etiqueta", "GUARDAR_ID:"+id);
        editor.putInt("id", id);
        editor.putString("metodo", metodoLogin);
        editor.putBoolean("sesion", true);
        editor.putString("nombre",nombre);
        editor.putString("apellidos",apellidos);
        editor.putString("email",email);

        editor.commit();

    }

    public static class CargarDatos extends AsyncTask<String, String, ArrayList<Producto2>>{

        Objeto[] objetos;
        Activity activity;
        SwipeStack pilaCartas;

        public CargarDatos(Objeto[] objetos, Activity activity){
            super();
            this.objetos = objetos;
            this.activity = activity;
            pilaCartas = (SwipeStack) activity.findViewById(R.id.pila_cartas);
        }

        @Override
        protected ArrayList<Producto2> doInBackground(String... strings) {

            ArrayList productos = new ArrayList<>();
            for (int x = 0; x < objetos.length; x++){
                Bitmap b = downloadBitmap(objetos[x].getId());
                productos.add(new Producto2(b, objetos[x].getDescripcion(), "", Integer.parseInt(objetos[x].getId())));
            }
            return productos;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Producto2> producto2s) {
            super.onPostExecute(producto2s);

            AdaptadorProductos adaptadorProductos = new AdaptadorProductos(activity, producto2s);
            pilaCartas.setAdapter(adaptadorProductos);
            pilaCartas.setListener(new SwipeStackCardListener(activity, producto2s));
            adaptadorProductos.notifyDataSetChanged();

        }

    }

    public static Bitmap downloadBitmap(String id){

        Bitmap bmp =null;
        try{
            URL ulrn = new URL("https://aux.swappie.tk/api/img/fotos_objetos/" + id + ".jpg");
            Log.e("etiqueta", "URL:"+ulrn.toString());
            HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
            con.setUseCaches(true);
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            if (null != bmp)
                return bmp;

        }catch(Exception e){}
        return bmp;

    }

}
