package giis.demo.envios.main;

import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import javax.swing.SwingUtilities;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import giis.demo.util.Database;
import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;
import giis.demo.envios.entity.UsuarioDTO;

import giis.demo.envios.HU_SolicitarEnvio.SolicitarEnvioController;
import giis.demo.envios.HU_SolicitarEnvio.SolicitarEnvioModel;
import giis.demo.envios.HU_SolicitarEnvio.SolicitarEnvioView;

import giis.demo.envios.HU_ConsultaEnvios.ConsultaEnviosController;
import giis.demo.envios.HU_ConsultaEnvios.ConsultaEnviosModel;
import giis.demo.envios.HU_ConsultaEnvios.ConsultaEnviosView;

import giis.demo.envios.HU_RecogidaPaquete.RecogidaPaqueteController;
import giis.demo.envios.HU_RecogidaPaquete.RecogidaPaqueteModel;
import giis.demo.envios.HU_RecogidaPaquete.RecogidaPaqueteView;

import giis.demo.envios.HU_LlegadaAlmacen.LlegadaAlmacenController;
import giis.demo.envios.HU_LlegadaAlmacen.LlegadaAlmacenModel;
import giis.demo.envios.HU_LlegadaAlmacen.LlegadaAlmacenView;

import giis.demo.envios.HU_SalidaAlmacen.SalidaAlmacenController;
import giis.demo.envios.HU_SalidaAlmacen.SalidaAlmacenModel;
import giis.demo.envios.HU_SalidaAlmacen.SalidaAlmacenView;

import giis.demo.envios.HU_APIEnvios.APIEnviosController;
import giis.demo.envios.HU_APIEnvios.APIEnviosModel;

public class DashboardMainController {
    private DashboardMainView view;
    private Database db;
    private UsuarioDTO activeUser;
    private HttpServer apiServer;
    private APIEnviosController apiController;

    private SolicitarEnvioController solicitarEnvioController;
    private ConsultaEnviosController consultaEnviosController;
    private RecogidaPaqueteController recogidaPaqueteController;
    private LlegadaAlmacenController llegadaAlmacenController;
    private SalidaAlmacenController salidaAlmacenController;

    public DashboardMainController(Database db, DashboardMainView view, UsuarioDTO usuario) {
        this.db = db;
        this.view = view;
        this.activeUser = usuario;
        
        initView();
        
        if ("Empresa".equals(activeUser.getRol())) {
            this.apiController = new APIEnviosController(new APIEnviosModel(db));
            iniciarServidorApi();
        }
        
        view.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (apiServer != null) {
                    apiServer.stop(0);
                }
            }
        });
    }

    private void initView() {
        view.getLblSesionInfo().setText(activeUser.getNombre() + " (" + activeUser.getDniNif() + ") - " + activeUser.getRol());
        String rol = activeUser.getRol();
        
        if (rol.equals("Usuario") || rol.equals("Gerente")) {
            SolicitarEnvioView solView = new SolicitarEnvioView();
            solicitarEnvioController = new SolicitarEnvioController(new SolicitarEnvioModel(db), solView, activeUser);
            view.getTabbedPane().addTab("Solicitar Envío (HU-02)", solView);
        }

        if (rol.equals("Usuario") || rol.equals("Empresa") || rol.equals("Gerente")) {
            ConsultaEnviosView consView = new ConsultaEnviosView();
            consultaEnviosController = new ConsultaEnviosController(new ConsultaEnviosModel(db), consView, activeUser);
            view.getTabbedPane().addTab("Mis Envíos (HU-08)", consView);
            consultaEnviosController.loadData();
            
            if (solicitarEnvioController != null) {
                solicitarEnvioController.setOnEnvioCreado(() -> {
                    consultaEnviosController.loadData();
                });
            }
        }

        if (rol.equals("Transportista") || rol.equals("Gerente")) {
            RecogidaPaqueteView recView = new RecogidaPaqueteView();
            recogidaPaqueteController = new RecogidaPaqueteController(new RecogidaPaqueteModel(db), recView, activeUser);
            view.getTabbedPane().addTab("Recogidas (HU-04)", recView);
            recogidaPaqueteController.loadData();
        }

        if (rol.equals("Operario") || rol.equals("Gerente")) {
            LlegadaAlmacenView llegView = new LlegadaAlmacenView();
            llegadaAlmacenController = new LlegadaAlmacenController(new LlegadaAlmacenModel(db), llegView, activeUser);
            view.getTabbedPane().addTab("Registrar Llegada (HU-06)", llegView);
        }

        if (rol.equals("Operario") || rol.equals("Gerente")) {
            SalidaAlmacenView salView = new SalidaAlmacenView();
            salidaAlmacenController = new SalidaAlmacenController(new SalidaAlmacenModel(db), salView, activeUser);
            view.getTabbedPane().addTab("Registrar Salida (HU-07)", salView);
            salidaAlmacenController.loadData();

            if (llegadaAlmacenController != null) {
                llegadaAlmacenController.setOnEnvioActualizado(() -> {
                    salidaAlmacenController.loadData();
                });
            }
        }

        if (rol.equals("Empresa") || rol.equals("Gerente")) {
            view.getTabbedPane().addTab("Portal Externo (HU-03)", view.crearTabPortalExterno());
            view.getBtnAbrirPortalExterno().addActionListener(e -> SwingUtil.exceptionWrapper(this::handleAbrirPortal));
        }

        view.getFrame().setVisible(true);
    }

    private void iniciarServidorApi() {
        try {
            if (apiServer != null) return;
            apiServer = HttpServer.create(new InetSocketAddress(8080), 0);
            apiServer.createContext("/api/envios", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) {
                    try {
                        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                        if ("OPTIONS".equals(exchange.getRequestMethod())) {
                            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
                            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                            exchange.sendResponseHeaders(204, -1);
                            return;
                        }

                        if ("POST".equals(exchange.getRequestMethod())) {
                            InputStream is = exchange.getRequestBody();
                            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                            
                            String jsonResponse = apiController.procesarPeticion(body);
                            
                            byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
                            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
                            exchange.sendResponseHeaders(200, responseBytes.length);
                            OutputStream os = exchange.getResponseBody();
                            os.write(responseBytes);
                            os.close();
                            
                            SwingUtilities.invokeLater(() -> {
                                if (consultaEnviosController != null) {
                                    consultaEnviosController.loadData();
                                }
                            });
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            apiServer.start();
        } catch (Exception ex) {
            System.err.println("No se pudo iniciar el servidor API en el puerto 8080: " + ex.getMessage());
        }
    }

    private void handleAbrirPortal() {
        try {
            File htmlFile = new File("src/main/resources/portal_empresa.html");
            if (htmlFile.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(htmlFile.toURI());
            } else {
                throw new ApplicationException("No se encuentra el archivo portal_empresa.html o Desktop no está soportado.");
            }
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }
}
