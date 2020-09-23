import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

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

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("houseUnit");

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

                EntityManager em = entityManagerFactory.createEntityManager();

                House newHouse = new House(inputCountry, inputCity, inputNumber);
                em.getTransaction().begin();
                em.persist(newHouse);
                em.getTransaction().commit();

                List<House> houseList = em.createQuery("from House").getResultList();
                System.out.printf("%-20s %-20s %-20s %-20s %n", "House_ID", "Страна", "Город", "Дом");
                houseList.stream()
                        .forEach(x -> System.out.printf("%-20s %-20s %-20s %-20s %n", x.getId(), x.getCountry(), x.getCity(), x.getApartment()));


            }else if(input.equals("2")) {
                System.out.println("2. Найти все дома из одного города.");
                EntityManager em = entityManagerFactory.createEntityManager();
                List<String> listHouse = em.createQuery("select distinct (city) from House order by city").getResultList();

                System.out.println("Города, которые представлены в БД:");
                listHouse.stream()
                        .forEach(x -> System.out.println(x));

                System.out.println("Введите название города:");
                String inputCity = scn.nextLine();

                List<House> houseFiltered = em.createQuery("from House where city = '"+inputCity+"'").getResultList();

                System.out.println("Дома, находящиеся в городе - " + inputCity + ":");
                System.out.printf("%-20s %-20s %-20s %-20s %n", "House_ID", "Страна", "Город", "Дом");
                houseFiltered.stream()
                        .forEach(x -> System.out.printf("%-20s %-20s %-20s %-20s %n", x.getId(), x.getCountry(), x.getCity(), x.getApartment()));


            }else if(input.equals("3")){
                System.out.println("3. Изменить номер дома на новый номер.");
                EntityManager em = entityManagerFactory.createEntityManager();

                List<House> houseList = em.createQuery("from House").getResultList();

                System.out.printf("%-20s %-20s %-20s %-20s %n", "House_ID", "Страна", "Город", "Дом");
                houseList.stream()
                        .forEach(x -> System.out.printf("%-20s %-20s %-20s %-20s %n", x.getId(), x.getCountry(), x.getCity(), x.getApartment()));

                System.out.println("Введите номер дома, который будет изменен на новый.");
                int oldApartment = Integer.valueOf(scn.nextLine());
                System.out.println("Введите новый номер дома:");
                int newApartment = Integer.valueOf(scn.nextLine());

                int idTemp = (Integer) em.createQuery("select id from House where apartment = "+oldApartment+"").getSingleResult();
                //int idTemp = em.createQuery("select id from House where apartment = "+oldApartment+"").getFirstResult();

                House changedHouse = (House)em.find(House.class, idTemp);
                changedHouse.setApartment(newApartment);
                em.getTransaction().begin();
                em.merge(changedHouse);
                em.getTransaction().commit();

                houseList = em.createQuery("from House").getResultList();

                System.out.printf("%-20s %-20s %-20s %-20s %n", "House_ID", "Страна", "Город", "Дом");
                houseList.stream()
                        .forEach(x -> System.out.printf("%-20s %-20s %-20s %-20s %n", x.getId(), x.getCountry(), x.getCity(), x.getApartment()));

            }else if(input.equals("4")){
                System.out.println("4. Удалить дом из базы по номеру дома и городу.");
                EntityManager em = entityManagerFactory.createEntityManager();
                List<House> houseList = em.createQuery("from House").getResultList();

                System.out.printf("%-20s %-20s %-20s %-20s %n", "House_ID", "Страна", "Город", "Дом");
                houseList.stream()
                        .forEach(x -> System.out.printf("%-20s %-20s %-20s %-20s %n", x.getId(), x.getCountry(), x.getCity(), x.getApartment()));

                System.out.println("Введите город, где находится удаляемый дом:");
                String inputCity = scn.nextLine();
                List<Integer> tempList = em.createQuery("select apartment from House where city = '"+inputCity+"'").getResultList();

                System.out.println("Номера домов в городе " + inputCity + ":");
                tempList.stream()
                        .forEach(x ->System.out.println(x));

                System.out.println("Введите номер дома:");
                int inputApartment = Integer.valueOf(scn.nextLine());

                int idTemp = (Integer) em.createQuery("select id from House where city = '"+inputCity+"' and apartment = "+inputApartment+"").getSingleResult();

                House deletedHouse = (House) em.find(House.class, idTemp);
                em.getTransaction().begin();
                em.remove(deletedHouse);
                em.getTransaction().commit();

                houseList = em.createQuery("from House").getResultList();

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
