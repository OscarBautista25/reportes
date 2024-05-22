import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.sql.*;

public class Factura {
    Connection conexion;
    PreparedStatement ps;
    ResultSet rs;

    void conectar(){
        try {
            conexion= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/usuarios311","root", "1234");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public Factura(){
        conectar();
    }
    String obtenerNombreUsuario(String factura) throws SQLException {
        conectar();
        String usuario = null;
        ps = conexion.prepareStatement("select usuario from factura where id_Factura = ?");
        ps.setString(1,factura);
        rs = ps.executeQuery();
        if(rs.next()){
            usuario=rs.getString("usuario");
        }
        return usuario;

    }
    String obtenerValorVenta(String factura) throws SQLException {
        conectar();
        String valorVenta = null;
        ps=conexion.prepareStatement("select valor_venta from factura where id_Factura = ?");
        ps.setString(1,factura);
        rs = ps.executeQuery();
        if(rs.next()){
            valorVenta=rs.getString("valor_venta");
        }
        return valorVenta;

    }
    String obtenerNombreVendedor(String factura) throws SQLException {
        conectar();
        String vendedor = null;
        ps=conexion.prepareStatement("select nombre_vendedor from factura where id_Factura = ?");
        ps.setString(1,factura);
        rs = ps.executeQuery();
        if(rs.next()){
            vendedor=rs.getString("nombre_vendedor");
        }
        return vendedor;

    }

    void generarFactura(String factura) throws SQLException {
        try{
            String usuario = obtenerNombreUsuario(factura);
            String valorVenta = obtenerValorVenta(factura);
            String vendedor = obtenerNombreVendedor(factura);

            Document documentoFactura = new Document();
            PdfWriter.getInstance(documentoFactura, new FileOutputStream("Factura.pdf"));
            documentoFactura.open();
            documentoFactura.add(new Paragraph("FACTURA DE COMPRA"));
            List list = new List();
            list.setSymbolIndent(12);
            list.setListSymbol("\u2022");
            list.add(new ListItem("Nombre del Comprador: "+usuario));
            list.add(new ListItem("Valor de la venta: "+valorVenta));
            list.add(new ListItem("Nombre del Vendedor: "+vendedor));
            documentoFactura.add(list);
            documentoFactura.close();
            System.out.println("La factura ha sido generada satisfactoriamente y sen encuentra alojada en la carpeta del proyecto");
        }catch (DocumentException | IOException e){
            System.out.println("Error al generar la factura "+e.getMessage());

        }
    }

    public static void main(String[] args) throws SQLException {
        Factura factura1 = new Factura();
        factura1.generarFactura("1001");
    }
}
