import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {

//    1. Сделать тоже самое, что и в предыдущем ДЗ, только вместо Human взять класс House с полями: страна, город, номер дома.
//    - Работу с БД сделать через JPA (entity manager);
//    - Настройку подключения сделать, используя файл properties;
//    - Общие классы (House) вынести в отдельный модуль Data;
//    - Все методы работы с сущностью House реализовать в отдельном модуле: HouseService;
//    - в родительском pom файле вынести все зависимости, а в дочернем их унаследовать.
//
//
//    Предыдущее ДЗ:
//    Создать класс Human с полями: имя, фамилия, город.
//    - создать с помощью hibernate несколько человек и сохранить их в таблице;
//    - найти людей из одного города;
//    - изменить фамилию человека на новую;
//    - удалить человека из базы по имени и фамилии.

        SessionFactory sessionFactory = HibernateUtil.createSessionFactory();

        String input = "";
        Scanner scn = new Scanner(System.in);

        while(!input.equals("5")){
            System.out.println("1. Создать с помощью hibernate несколько домов и сохранить их в таблице.");
            System.out.println("2. Найти все дома из одного города.");
            System.out.println("3. Изменить номер дома на новый номер.");
            System.out.println("4. Удалить дом из базы по номеру дома и городу.");
            System.out.println("5. Выход.");

            input = scn.nextLine();
            if(input.equals("1")) {
                System.out.println("1. Создать с помощью hibernate несколько домов и сохранить их в таблице.");
                System.out.println("Введите страну:");
                String inputCountry = scn.nextLine();
                System.out.println("Введите город:");
                String inputCity = scn.nextLine();
                System.out.println("Введите номер дома:");
                int inputNumber = Integer.valueOf(scn.nextLine());

                Session session = sessionFactory.openSession();
                session.beginTransaction();

                try{
                    House newHouse = new House(inputCountry, inputCity, inputNumber);
                    session.save(newHouse);
                    session.getTransaction().commit();
                }catch(Exception e){
                    System.out.println(e);
                    session.getTransaction().rollback();
                }

                Query<House> query = session.createQuery("from House");
                List<House> houseList = query.getResultList();
                System.out.printf("%-20s %-20s %-20s %-20s %n", "House_ID", "Страна", "Город", "Дом");
                houseList.stream()
                        .forEach(x -> System.out.printf("%-20s %-20s %-20s %-20s %n", x.getId(), x.getCountry(), x.getCity(), x.getApartment()));


            }else if(input.equals("2")) {
                System.out.println("2. Найти все дома из одного города.");
                Session session = sessionFactory.openSession();
                Query<String> query = session.createQuery("select distinct (city) from House order by city");
                List<String> listHouse = query.getResultList();
                System.out.println("Города, которые представлены в БД:");
                listHouse.stream()
                        .forEach(x -> System.out.println(x));

                System.out.println("Введите название города:");
                String inputCity = scn.nextLine();

                Query<House> query2 = session.createQuery("from House where city = :cityParam");
                query2.setParameter("cityParam", inputCity);

                List<House> houseFiltered = query2.getResultList();
                System.out.println("Дома, находящиеся в городе - " + inputCity + ":");
                System.out.printf("%-20s %-20s %-20s %-20s %n", "House_ID", "Страна", "Город", "Дом");
                houseFiltered.stream()
                        .forEach(x -> System.out.printf("%-20s %-20s %-20s %-20s %n", x.getId(), x.getCountry(), x.getCity(), x.getApartment()));


            }else if(input.equals("3")){
                System.out.println("3. Изменить номер дома на новый номер.");
                Session session = sessionFactory.openSession();
                Query<House> query = session.createQuery("from House");
                List<House> houseList = query.getResultList();

                System.out.printf("%-20s %-20s %-20s %-20s %n", "House_ID", "Страна", "Город", "Дом");
                houseList.stream()
                        .forEach(x -> System.out.printf("%-20s %-20s %-20s %-20s %n", x.getId(), x.getCountry(), x.getCity(), x.getApartment()));

                System.out.println("Введите номер дома, который будет изменен на новый.");
                int oldApartment = Integer.valueOf(scn.nextLine());
                System.out.println("Введите новый номер дома:");
                int newApartment = Integer.valueOf(scn.nextLine());

                Query<House> query2 = session.createQuery("from House where apartment = :apartmentParam");
                query2.setParameter("apartmentParam", oldApartment);
                House house = query2.getSingleResult();
                house.setApartment(newApartment);
                session.beginTransaction();

                try{
                    session.save(house);
                    session.getTransaction().commit();
                }catch(Exception e){
                    System.out.println(e);
                    session.getTransaction().rollback();
                }

                Query<House> query3 = session.createQuery("from House");
                houseList = query3.getResultList();

                System.out.println("Список с изменненным номером дома.");
                System.out.printf("%-20s %-20s %-20s %-20s %n", "Human_ID", "Имя", "Фамилия", "Город");
                houseList.stream()
                        .forEach(x -> System.out.printf("%-20s %-20s %-20s %-20s %n", x.getId(), x.getCountry(), x.getCity(), x.getApartment()));

            }else if(input.equals("4")){
                System.out.println("4. Удалить дом из базы по номеру дома и городу.");
                Session session = sessionFactory.openSession();
                Query<House> query = session.createQuery("from House");
                List<House> houseList = query.getResultList();

                System.out.printf("%-20s %-20s %-20s %-20s %n", "House_ID", "Страна", "Город", "Дом");
                houseList.stream()
                        .forEach(x -> System.out.printf("%-20s %-20s %-20s %-20s %n", x.getId(), x.getCountry(), x.getCity(), x.getApartment()));

                System.out.println("Введите город, где находится удаляемый дом:");
                String inputCity = scn.nextLine();
                Query<House> query2 = session.createQuery("from House where city = :cityParam");
                query2.setParameter("cityParam", inputCity);
                List<House> tempList = query2.getResultList();

                System.out.println("Номера домов в городе " + inputCity + ":");
                tempList.stream()
                        .forEach(x -> System.out.printf("%-20s %n", x.getApartment()));

                System.out.println("Введите номер дома:");
                int inputApartment = Integer.valueOf(scn.nextLine());

                Query<House> query3 = session.createQuery("from House where city = :cityParam and apartment = :apartmentParam");
                query3.setParameter("cityParam", inputCity);
                query3.setParameter("apartmentParam", inputApartment);

                House house = query3.getSingleResult();
                session.beginTransaction();

                try{
                    session.remove(house);
                    session.getTransaction().commit();
                }catch(Exception e){
                    System.out.println(e);
                    session.getTransaction().rollback();
                }

                Query<House> query4 = session.createQuery("from House");
                houseList = query4.getResultList();

                System.out.printf("%-20s %-20s %-20s %-20s %n", "House_ID", "Страна", "Город", "Дом");
                houseList.stream()
                        .forEach(x -> System.out.printf("%-20s %-20s %-20s %-20s %n", x.getId(), x.getCountry(), x.getCity(), x.getApartment()));

            }else if(input.equals("5")){
                System.out.println("5. Выход. Программа завершила работу.");

            }else{
                System.out.println("Некорректный ввод.");

            }
        }
    }
}
