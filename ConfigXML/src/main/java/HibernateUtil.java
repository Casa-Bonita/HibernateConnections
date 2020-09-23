import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    public static SessionFactory createSessionFactory(){

        Configuration configuration = new Configuration().configure();
        return configuration.buildSessionFactory();
    }
}
