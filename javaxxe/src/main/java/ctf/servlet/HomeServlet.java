package ctf.servlet;

import ctf.dao.ServiceDao;
import ctf.dao.impl.ServiceDaoImpl;
import ctf.model.Service;
import ctf.utils.XmlUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.regex.Pattern;

@WebServlet(name = "HomeServlet", urlPatterns = "/")
public class HomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getParameter("uid");

        Service service;
        ServiceDao dao = ServiceDaoImpl.getInstance();
        if (uid != null && Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$").matcher(uid).matches()) {
            service = dao.getServiceById(uid);
        } else {
            service = dao.getServiceByName("default");
        }
        if (service != null) {
            req.setAttribute("service", service);
        } else {
            req.setAttribute("error", "Service Not Found");
        }
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        PrintWriter writer = resp.getWriter();

        String xmlString = req.getParameter("serviceCall");
        xmlString = XmlUtil.toXML(xmlString);

        if (XmlUtil.check(xmlString)) {
            resp.setStatus(403);
            writer.println(XmlUtil.toXML(new Exception("Bad words detected")));
            return;
        }

        String err = XmlUtil.validate(xmlString);

        if (err == null) {
            Object parsed = XmlUtil.parse(xmlString);
            if (parsed instanceof Service) {

                Service service = (Service) parsed;
                String uid = UUID.randomUUID().toString();
                service.setUid(uid);
                ServiceDaoImpl.getInstance().addService(service);

                writer.println("<success>/index.jsp?uid=" + uid + "<success>");
            } else {
                resp.setStatus(500);
                writer.println(parsed);
            }
        } else {
            resp.setStatus(500);
            writer.println(err);
        }
    }
}
