package InterfaceDao;


import Models.Session;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.jdbc.JDBCPieDataset;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface SessionDAO {
    List<Session> getAllSession();
    void createSession(Session session);
    Session readSession(int week, String date, String time);
    void updateSession(Session session);
    void deleteSession(Session session);
    List<Session> getInfomatiqueSession();
    List<Session> getMathematiqueSession();
    List<Session> getAnglaisSession();
    JDBCPieDataset readData();
}
