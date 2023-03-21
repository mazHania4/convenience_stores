package com.ipc2.convenience_stores.web;

import com.ipc2.convenience_stores.data.*;
import com.ipc2.convenience_stores.models.*;
import com.ipc2.convenience_stores.models.devolutions.*;
import com.ipc2.convenience_stores.models.incidences.*;
import com.ipc2.convenience_stores.models.requisitions.*;
import com.ipc2.convenience_stores.models.shipments.*;
import com.ipc2.convenience_stores.models.users.*;
import com.ipc2.convenience_stores.models.stores.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/entryFile-servlet")
@MultipartConfig
public class EntryFileServlet extends HttpServlet {

    private JSONObject jsonObject;
    private Connection connection;
    private ProductDB productDB;
    private UserDB userDB;
    private StoreDB storeDB;
    private RequisitionDB requisitionDB;
    private ShipmentDB shipmentDB;

    private List<Integer[]> warehouseUser_Stores;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        connection = (Connection) req.getSession().getAttribute("connection");
        productDB = new ProductDB(connection);
        userDB = new UserDB(connection);
        storeDB = new StoreDB(connection);
        requisitionDB = new RequisitionDB(connection);
        shipmentDB = new ShipmentDB(connection);
        Part file = req.getPart("entryFile");
        InputStream fileContent = file.getInputStream();
        try {
            jsonObject = (JSONObject) new JSONParser().parse(
                    new InputStreamReader(fileContent, StandardCharsets.UTF_8));
            initializeDataBase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        resp.sendRedirect("login.jsp");
    }

    private void initializeDataBase() {
        initializeProducts();
        initializeAdminUsers();
        initializeSupervisorUsers();
        initializeWarehouseUsers();
        initializeStoresAndCatalogs();
        initializeStoreUsers();
        initializeRequisitions();
        initializeShipments();
        initializeIncidences();
        initializeDevolution();
        System.out.println("-------DB initialized-------");
    }

    private void initializeProducts(){
        JSONArray products = (JSONArray) jsonObject.get("productos");
        for (Object producto : products) {
            JSONObject product = (JSONObject) producto;
            var id = getInt(product, "codigo");
            var name = product.get("nombre");
            var cost = getDouble(product, "costo");
            var price = getDouble(product, "precio");
            var stock = getInt(product, "existencias");
            productDB.createProductGeneralCatalog(new Product(id, (String)name, stock, cost, price));
        }
    }

    private void initializeAdminUsers(){
        JSONArray admins = (JSONArray) jsonObject.get("admins");
        for (Object user : admins) {
            JSONObject admin = (JSONObject) user;
            var id = getInt(admin, "codigo");
            var name = admin.get("nombre");
            var username = admin.get("username");
            var password = admin.get("password");
            User newUser = new User(id, UserType.ADMIN_USER, (String)name, (String)username, "", (String)password, null );
            newUser.setPassword((String)password);
            userDB.createUser(newUser);
        }
    }

    private void initializeSupervisorUsers(){
        JSONArray supervisors = (JSONArray) jsonObject.get("supervisores");
        for (Object user : supervisors) {
            JSONObject supervisor = (JSONObject) user;
            var id = getInt(supervisor, "codigo");
            var name = supervisor.get("nombre");
            var username = supervisor.get("username");
            var password = supervisor.get("password");
            var email = supervisor.get("email");
            User newUser = new User(id, UserType.SUPERVISOR_USER, (String)name, (String)username, (String)email, (String)password, null );
            newUser.setPassword((String)password);
            userDB.createUser(newUser);
        }
    }

    private void initializeWarehouseUsers(){
        warehouseUser_Stores = new ArrayList<>();
        JSONArray warehouseUsers = (JSONArray) jsonObject.get("encargadosBodega");
        for (Object user : warehouseUsers) {
            JSONObject warehouseUser = (JSONObject) user;
            var id = getInt(warehouseUser, "codigo");
            var name = warehouseUser.get("nombre");
            var username = warehouseUser.get("username");
            var password = warehouseUser.get("password");
            var email = warehouseUser.get("email");
            User newUser = new User(id, UserType.WAREHOUSE_USER, (String)name, (String)username, (String)email, (String)password, null );
            newUser.setPassword((String)password);
            userDB.createUser(newUser);
            JSONArray stores = (JSONArray) warehouseUser.get("tiendas");
            for (Object store : stores) {
                int storeId = ((Number) store).intValue();
                warehouseUser_Stores.add(new Integer[]{id, storeId});
            }
        }
    }

    private void initializeStoresAndCatalogs(){
        JSONArray stores = (JSONArray) jsonObject.get("tiendas");
        CatalogDB catalogDB = new CatalogDB(connection);
        for (Object st : stores) {
            JSONObject store = (JSONObject) st;
            var id = getInt(store, "codigo");
            var address = store.get("direccion");
            StoreType type = store.get("tipo").equals("supervisada") ? StoreType.SUPERVISED : StoreType.NOT_SUPERVISED;
            var whUser = userDB.getUser(getWareHouseUserId(id), UserType.WAREHOUSE_USER);
            if (whUser.isPresent()){
                Store newStore = new Store( id, whUser.get(), (String)address, type);
                storeDB.createStore(newStore);
                JSONArray products = (JSONArray) store.get("productos");
                for (Object item : products) {
                    JSONObject product = (JSONObject) item;
                    var stock = getInt(product, "existencias");
                    var productI = productDB.getProductGeneralCatalog(getInt(product, "codigo"));
                    productI.ifPresent(value -> catalogDB.addProductToCatalog(newStore, value, stock));
                }
            }
        }
    }

    private void initializeStoreUsers(){
        JSONArray stUsers = (JSONArray) jsonObject.get("usuariostienda");
        for (Object user : stUsers) {
            JSONObject stUser = (JSONObject) user;
            var id = getInt(stUser, "codigo");
            var name = stUser.get("nombre");
            var storeId = getInt(stUser, "tienda");
            var username = stUser.get("username");
            var password = stUser.get("password");
            var email = stUser.get("email");
            var store = storeDB.getStore(storeId);
            if (store.isPresent()){
                User newUser = new User(id, UserType.STORE_USER, (String)name, (String)username, (String)email, (String)password, store.get() );
                newUser.setPassword((String)password);
                userDB.createUser(newUser);
            }
        }
    }

    private void initializeRequisitions(){
        JSONArray requisitions = (JSONArray) jsonObject.get("pedidos");
        for (Object requisition : requisitions) {
            JSONObject req = (JSONObject) requisition;
            List<Product> products = new ArrayList<>();
            var id = getInt(req, "id");
            var store = storeDB.getStore(getInt(req, "tienda"));
            var user = userDB.getUser(getInt(req, "usuario"), UserType.STORE_USER);
            var dateTime = (LocalDate.parse((String) req.get("fecha")).atStartOfDay());
            var state = getRequisitionState((String) req.get("estado"));
            JSONArray items = (JSONArray) req.get("productos");
            for (Object item : items) {
                JSONObject product = (JSONObject) item;
                var productId = getInt(product, "codigo");
                var cost = getDouble(product, "costoU");
                var amount = getInt(product, "cantidad");
                products.add(new Product(productId, " ", amount, cost, 0.0));
            }
            if (store.isPresent() && user.isPresent()){
                requisitionDB.createRequisition(new Requisition(id, store.get(), user.get(), state, 0.0 , dateTime, products));
            }
        }
    }

    private void initializeShipments(){
        JSONArray shipments = (JSONArray) jsonObject.get("envios");
        for (Object shipmentObj : shipments) {
            JSONObject shipment = (JSONObject) shipmentObj;
            List<Product> products = new ArrayList<>();
            var id = getInt(shipment, "id");
            var requisition = requisitionDB.getRequisition(getInt(shipment, "pedido"));
            var store = storeDB.getStore(getInt(shipment, "tienda"));
            var user = userDB.getUser(getInt(shipment, "usuario"), UserType.WAREHOUSE_USER);
            var dispatchedDateTime = (LocalDate.parse((String) shipment.get("fechaSalida")).atStartOfDay());
            var receivedDateTime = shipment.get("fechaRecibido").equals("")
                    ? null
                    : (LocalDate.parse((String) shipment.get("fechaRecibido")).atStartOfDay());
            var state = getShipmentState((String) shipment.get("estado"));
            JSONArray items = (JSONArray) shipment.get("productos");
            for (Object item : items) {
                JSONObject product = (JSONObject) item;
                var productId = getInt(product, "codigo");
                var cost = getDouble(product, "costoU");
                var amount = getInt(product, "cantidad");
                products.add(new Product(productId, " ", amount, cost, 0.0));
            }
            if (store.isPresent() && user.isPresent() && requisition != null){
                shipmentDB.createShipment(new Shipment(id, store.get(), user.get(), requisition, state, dispatchedDateTime , receivedDateTime, 0.0, products));
            }
        }
    }


    private void initializeIncidences(){
        IncidenceDB incidenceDB = new IncidenceDB(connection);
        JSONArray incidences = (JSONArray) jsonObject.get("incidencias");
        for (Object incidenceObj : incidences) {
            JSONObject incidence = (JSONObject) incidenceObj;
            List<Product> products = new ArrayList<>();
            var id = getInt(incidence, "id");
            var shipment = shipmentDB.getShipment(getInt(incidence, "envio"));
            var store = storeDB.getStore(getInt(incidence, "tienda"));
            var user = userDB.getUser(getInt(incidence, "usuario"), UserType.STORE_USER);
            var dateTime = (LocalDate.parse((String) incidence.get("fecha")).atStartOfDay());
            var state = getIncidenceState((String) incidence.get("estado"));
            var solution = incidence.get("solucion");
            JSONArray items = (JSONArray) incidence.get("productos");
            for (Object item : items) {
                JSONObject product = (JSONObject) item;
                var productId = getInt(product, "codigo");
                var amount = getInt(product, "cantidad");
                var reason = getIncidenceReason((String)product.get("motivo"));
                products.add(new Product(productId, " ", amount, 0.0, 0.0, reason));
            }
            if (store.isPresent() && user.isPresent() && shipment != null){
                incidenceDB.createIncidence(new Incidence(id, store.get(), user.get(), shipment, state, dateTime, (String) solution, products));
            }
        }
    }

    private void initializeDevolution(){
        DevolutionDB devolutionDB = new DevolutionDB(connection);
        JSONArray devolutions = (JSONArray) jsonObject.get("devoluciones");
        for (Object devolutionsObj : devolutions) {
            JSONObject devolution = (JSONObject) devolutionsObj;
            List<Product> products = new ArrayList<>();
            var id = getInt(devolution, "id");
            var shipment = shipmentDB.getShipment(getInt(devolution, "envio"));
            var store = storeDB.getStore(getInt(devolution, "tienda"));
            var user = userDB.getUser(getInt(devolution, "usuario"), UserType.STORE_USER);
            var dateTime = (LocalDate.parse((String) devolution.get("fecha")).atStartOfDay());
            var state = getDevolutionState((String) devolution.get("estado"));
            JSONArray items = (JSONArray) devolution.get("productos");
            for (Object item : items) {
                JSONObject product = (JSONObject) item;
                var productId = getInt(product, "codigo");
                var cost = getDouble(product, "costo");
                var amount = getInt(product, "cantidad");
                var reason = getDevolutionReason((String)product.get("motivo"));
                products.add(new Product(productId, " ", amount, cost, 0.0, reason));
            }
            if (store.isPresent() && user.isPresent() && shipment != null){
                devolutionDB.createDevolution(new Devolution(id, store.get(), user.get(), shipment, state, dateTime, 0.0, products));
            }
        }
    }

    private double getDouble(JSONObject jsonObject, String key){
        var value = jsonObject.get(key);
        if (value != null) {
            return ((Number) value).doubleValue();
        } else {
            return 0.0;
        }
    }

    private int getInt(JSONObject jsonObject, String key){
        var value = jsonObject.get(key);
        if (value != null) {
            return ((Number) value).intValue();
        } else {
            return 0;
        }
    }

    private RequisitionState getRequisitionState(String value){
        switch (value){
            case "COMPLETADO": return RequisitionState.COMPLETED;
            case "SOLICITADO": return RequisitionState.REQUESTED;
            case "RECHAZADO": return RequisitionState.REJECTED;
            case "ENVIADO": return  RequisitionState.SENT;
            case "PENDIENTE": return RequisitionState.PENDING_REVIEW;
        }
        return null;
    }

    private DevolutionState getDevolutionState(String value){
        switch (value){
            case "ACTIVA": return DevolutionState.ACTIVE;
            case "RECHAZADA": return DevolutionState.REJECTED;
            case "ACEPTADA": return  DevolutionState.ACCEPTED;
        }
        return null;
    }

    private ShipmentState getShipmentState(String value) {
        switch (value){
            case "DESPACHADO": return ShipmentState.DISPATCHED;
            case "RECIBIDO": return ShipmentState.RECEIVED;
        }
        return null;
    }

    private IncidenceState getIncidenceState(String value){
        switch (value){
            case "SOLUCIONDA": return IncidenceState.SOLVED;
            case "ACTIVA": return IncidenceState.ACTIVE;
        }
        return null;
    }

    private IncidenceReason getIncidenceReason(String value){
        switch (value){
            case "PRODUCTO EQUIVOCADO": return IncidenceReason.WRONG_PRODUCT;
            case "PRODUCTO DAÑADO": return IncidenceReason.DAMAGED_PRODUCT;
            case "PRODUCTO NO SOLICITADO": return IncidenceReason.UNSOLICITED_PRODUCT;
            case "FALTANTE DE PRODUCTO": return IncidenceReason.MISSING_PRODUCT;
            case "SOBRANTE DE PRODUCTO": return IncidenceReason.EXCESS_PRODUCT;
        }
        return null;
    }

    private DevolutionReason getDevolutionReason(String value){
        switch (value){
            case "PRODUCTO EQUIVOCADO": return DevolutionReason.WRONG_PRODUCT;
            case "PRODUCTO DAÑADO": return DevolutionReason.DAMAGED_PRODUCT;
            case "PRODUCTO NO SOLICITADO": return DevolutionReason.UNSOLICITED_PRODUCT;
            case "SOBRANTE DE PRODUCTO": return DevolutionReason.EXCESS_PRODUCT;
        }
        return null;
    }

    private int getWareHouseUserId(int storeId){
        for (Integer[] record : warehouseUser_Stores) {
            if (record[1] == storeId){
                return record[0];
            }
        }
        return 0;
    }
}
