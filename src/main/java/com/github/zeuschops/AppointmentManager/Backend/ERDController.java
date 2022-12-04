package com.github.zeuschops.AppointmentManager.Backend;

import com.github.gaijinkindred.AppointmentManager.ERD.*;
import com.github.zeuschops.AppointmentManager.Main;
import com.github.zeuschops.AppointmentManager.ERD.*;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

public class ERDController {
    private HashMap<Integer, Address> addressMap = new HashMap<Integer, Address>();
    private HashMap<Integer, Appointment> appointmentMap = new HashMap<Integer, Appointment>();
    private HashMap<Integer, City> cityMap = new HashMap<Integer, City>();
    private HashMap<Integer, Country> countryMap = new HashMap<Integer, Country>();
    private HashMap<Integer, Customer> customerMap = new HashMap<Integer, Customer>();
    private HashMap<Integer, User> userMap = new HashMap<Integer, User>();

    private static ERDController CONTROLLERINSTANCE;
    public static ERDController getInstance() {
        return CONTROLLERINSTANCE == null ? CONTROLLERINSTANCE = new ERDController() : CONTROLLERINSTANCE;
    }

    private static DatabaseConnector DATABASEINSTANCE = DatabaseConnector.getInstance();

    public ERDController() {
        String[] requests = {"address", "appointment", "city", "country", "customer"};
        ArrayList<Appointment> scheduledAppointments = new ArrayList<Appointment>();
        for(int i = 0; i < requests.length; i++) {
            ResultSet rs = DATABASEINSTANCE.request("SELECT * FROM U06YWn." + requests[i]);
            try {
                rs.next();
                do {
                    String contId = rs.getString(requests[i] + "Id");
                    switch (i) {
                        case 0:
                            ResultSet rs2 = DATABASEINSTANCE.request("SELECT cityId, addressId FROM U06YWn." +
                                    "address WHERE addressId='" + rs.getInt("addressId") + "'");
                            rs2.next();
                            int cityId = rs2.getInt("cityId");
                            Date date = rs.getDate("createDate");
                            Address addr = new Address(rs.getInt("addressId"),
                                    rs.getString("address"),
                                    rs.getString("address2"),
                                    cityId,
                                    rs.getString("postalCode"),
                                    rs.getString("phone"),
                                    convertToLocalFromUTC(rs.getDate("createDate")),
                                    rs.getString("createdBy"),
                                    convertToLocalFromUTC(rs.getTimestamp("lastUpdate")),
                                    rs.getString("lastUpdateBy"));
                            addressMap.put(addr.getAddressId(), addr);
                            break;
                        case 1:
                            Appointment appo = new Appointment(rs.getInt("appointmentId"),
                                    rs.getInt("customerId"),
                                    rs.getInt("userId"),
                                    rs.getString("title"),
                                    rs.getString("description"),
                                    rs.getString("location"),
                                    rs.getString("contact"),
                                    rs.getString("type"),
                                    rs.getString("URL"),
                                    convertToLocalFromUTC(rs.getTimestamp("start")),
                                    convertToLocalFromUTC(rs.getTimestamp("end")),
                                    convertToLocalFromUTC(rs.getDate("createDate")),
                                    rs.getString("createdBy"),
                                    convertToLocalFromUTC(rs.getTimestamp("lastUpdate")),
                                    rs.getString("lastUpdateBy"));
                            appointmentMap.put(appo.getAppointmentId(), appo);
                            long appointmentTime = appo.getStartDate().getTime();
                            System.out.println(appo.getStartDate().toString() + " | " + new java.util.Date().toString());
                            System.out.println(appo.getStartDate().getTime() + " | " + System.currentTimeMillis() + " | " + (appo.getStartDate().getTime() - System.currentTimeMillis()) + " | " + (1000 * 60 * 15));
                            System.out.println((appointmentTime - (System.currentTimeMillis() + (1000 * 60 * 15)) <= 0) + " | " + (appointmentTime - System.currentTimeMillis() > -1));
                            if (appointmentTime - (System.currentTimeMillis() + (1000 * 60 * 15)) <= 0 && appointmentTime - System.currentTimeMillis() > -1) {
                                scheduledAppointments.add(appo);
                            }
                            break;
                        case 2:
                            City city = new City(rs.getInt("cityId"),
                                    rs.getString("city"),
                                    rs.getInt("countryId"),
                                    convertToLocalFromUTC(rs.getDate("createDate")),
                                    rs.getString("createdBy"),
                                    convertToLocalFromUTC(rs.getTimestamp("lastUpdate")),
                                    rs.getString("lastUpdateBy"));
                            cityMap.put(city.getCityId(), city);
                            break;
                        case 3:
                            Country country = new Country(rs.getInt("countryId"),
                                    rs.getString("country"),
                                    convertToLocalFromUTC(rs.getDate("createDate")),
                                    rs.getString("createdBy"),
                                    convertToLocalFromUTC(rs.getTimestamp("lastUpdate")),
                                    rs.getString("lastUpdateBy"));
                            countryMap.put(country.getCountryId(), country);
                            break;
                        case 4:
                            Customer custom = new Customer(rs.getInt("customerId"),
                                    rs.getString("customerName"),
                                    rs.getInt("addressId"),
                                    rs.getInt("active"),
                                    convertToLocalFromUTC(rs.getDate("createDate")),
                                    rs.getString("createdBy"),
                                    convertToLocalFromUTC(rs.getTimestamp("lastUpdate")),
                                    rs.getString("lastUpdateBy"));
                            customerMap.put(custom.getCustomerId(), custom);
                            break;
                        default:
                            break;
                    }
                } while (rs.next());
            } catch (SQLException e) {
                if (Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newError("Erreur de synchronisation de base de données", "Veuillez vérifier " +
                            "votre connexion et réessayer. Sortant...");
                } else if (Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newError("Error de sincronización de la base de datos", "Por favor, compruebe " +
                            "la conexión y vuelva a intentarlo. Saliendo...");
                } else {
                    Main.newError("Database synchronization error", "Please check your connection and " +
                            "try again. Exiting..");
                }
                System.exit(-1);
            }
        }
        System.out.println("Scheduled appointments: " + scheduledAppointments.size());
        //Entire appointment stuff for upcoming appointments;
        for(Appointment a : scheduledAppointments) {
            System.out.println(a.getTitle() + " | " +  a.getStartDate().toString());
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newAlert("Rendez-vous Bientôt", "Rendez-vous à venir " +
                        a.getStartDate().toString() + " avec titre " + a.getTitle() + ".");
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newAlert("Cita Pronto", "Próxima cita en " +
                        a.getStartDate().toString() + " con titulo: " + a.getTitle() + ".");
            } else {
                Main.newAlert("Appointment Soon", "Upcoming appointment at " +
                        a.getStartDate().toString() + " with title: " + a.getTitle() + ".");
            }
        }
    }

    public ArrayList<Country> getCountries() {
        ArrayList<Country> countries = new ArrayList<Country>();
        for(Integer i : countryMap.keySet()) {
            countries.add(countryMap.get(i));
        }
        return countries;
    }

    public Country getCountry(int key) {
        return countryMap.get(key);
    }

    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = new ArrayList<Customer>();
        for(Integer i : customerMap.keySet()) {
            customers.add(customerMap.get(i));
        }
        return customers;
    }

    public Customer getCustomer(int key) {
        return customerMap.get(key);
    }

    public ArrayList<Address> getAddresses() {
        ArrayList<Address> addresses = new ArrayList<Address>();
        for(Integer i : addressMap.keySet()) {
            addresses.add(addressMap.get(i));
        }
        return addresses;
    }

    public Address getAddress(int key) {
        return addressMap.get(key);
    }

    public ArrayList<Appointment> getAppointments() {
        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        for(Integer i : appointmentMap.keySet()) {
            appointments.add(appointmentMap.get(i));
        }
        return appointments;
    }

    public Appointment getAppointment(int key) {
        return appointmentMap.get(key);
    }

    public ArrayList<City> getCities() {
        ArrayList<City> cities = new ArrayList<City>();
        for(Integer i : cityMap.keySet()) {
            cities.add(cityMap.get(i));
        }
        return cities;
    }

    public City getCity(int key) {
        return cityMap.get(key);
    }

    public Date convertToUTCFromLocal(Date localDate) {
        return new Date(convertToUTCFromLocal(new Timestamp(localDate.getTime())).getTime());
    }

    public Timestamp convertToUTCFromLocal(Timestamp localDate) {
        long tz = 0;
        if(Main.lmbIdent == LMBIdentifier.PHOENIX) {
            tz = 8 * 60 * 60 *1000;
        } else if(Main.lmbIdent == LMBIdentifier.NEWYORK) {
            tz = 5 * 60 * 60 * 1000;
        } else {
            tz = -1 * 60 * 60 * 1000;
        }
        return new Timestamp(localDate.getTime() + tz); //convert forward/back here
    }

    public Date convertToLocalFromUTC(Date utcDate) {
        return new Date(convertToLocalFromUTC(new Timestamp(utcDate.getTime())).getTime());
    }

    public Timestamp convertToLocalFromUTC(Timestamp utcTimestamp) {
        long offset = 0;//(date.getTime() - initDate.getTime()); // This is fine
        TimeZone tz = TimeZone.getDefault();
        long hour = 1000 * 60 * 60;
        if(Main.lmbIdent == LMBIdentifier.PHOENIX && !tz.getID().equalsIgnoreCase("America/Phoenix")) {
            //tz.setID("America/Phoenix");
            offset = (long)(Math.signum(tz.getRawOffset())) * (Math.abs(tz.getRawOffset()) - (8*hour));
        } else if(Main.lmbIdent == LMBIdentifier.NEWYORK && !tz.getID().equalsIgnoreCase("America/New_York")) {
            //tz.setID("America/New_York");
            offset = (long)(Math.signum(tz.getRawOffset())) * (Math.abs(tz.getRawOffset()) - (5*hour));
        } else if(Main.lmbIdent == LMBIdentifier.LONDON && !tz.getID().equalsIgnoreCase("England/London")) {
            //tz.setID("England/London");
            offset = (long)(Math.signum(tz.getRawOffset())) * (Math.abs(tz.getRawOffset()) + (hour));
        }
        Timestamp newTimestamp = new Timestamp(utcTimestamp.getTime() - offset);
        return newTimestamp;
    }

    public void updateCustomer(Customer customer, String customerName, int addressId, int active, Timestamp lastUpdate,
                               String lastUpdateBy) {
        try {
            //Server-side:
            DateFormat df = DateFormat.getInstance();
            if(Main.lmbIdent == LMBIdentifier.PHOENIX) {
                df.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
            } else if(Main.lmbIdent == LMBIdentifier.LONDON) {
                df.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            } else {
                df.setTimeZone(TimeZone.getTimeZone("America/New York"));
            }
            Timestamp newTimestamp = new Timestamp(df.parse(df.format(lastUpdate)).getTime());
            String ins = "UPDATE customer SET customerName='" + customerName + "', addressId='" + addressId
                    + "', active='" + active + "', lastUpdate='" + newTimestamp.toString() + "', lastUpdateBy='"
                    + lastUpdateBy + "' WHERE customerId='" + customer.getCustomerId() + "'";
            DatabaseConnector.getInstance().request(ins);

            //Local:
            customer.setCustomerName(customerName);
            customer.setAddressId(addressId);
            customer.setActive(active);
            customer.setLastUpdate(lastUpdate);
            customer.setLastUpdateBy(lastUpdateBy);
        } catch(ParseException e) {
            updateError();
        }
    }

    public void updateAppointment(Appointment appointment, int customerId, int userId, String title, String description,
                                  String location, String type, Timestamp start, Timestamp end, Timestamp lastUpdate,
                                  String lastUpdateBy) {
        try {
            DateFormat df = DateFormat.getInstance();
            if(Main.lmbIdent == LMBIdentifier.PHOENIX) {
                df.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
            } else if(Main.lmbIdent == LMBIdentifier.LONDON) {
                df.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            } else {
                df.setTimeZone(TimeZone.getTimeZone("America/New York"));
            }
            Timestamp newTimestamp = new Timestamp(df.parse(df.format(lastUpdate)).getTime());
            String ins = "UPDATE appointment SET customerId='" + customerId + "', userId='" + userId + "', title='"
                    + title + "', description='" + description + "', location='" + location + "', type='" + type
                    + "', start='" + convertToUTCFromLocal(start).toString() + "', end='"
                    + convertToUTCFromLocal(end).toString() + "', lastUpdate='"
                    + convertToUTCFromLocal(lastUpdate).toString() + "', lastUpdateBy='" + lastUpdateBy
                    + "' WHERE appointmentId='" + appointment.getAppointmentId() + "'";
            DatabaseConnector.getInstance().request(ins);

            appointment.setCustomerId(customerId);
            appointment.setUserId(userId);
            appointment.setTitle(title);
            appointment.setDescription(description);
            appointment.setLocation(location);
            appointment.setType(type);
            appointment.setStartDate(start);
            appointment.setEndDate(end);
            appointment.setLastUpdate(lastUpdate);
            appointment.setLastUpdateBy(lastUpdateBy);
        }  catch(ParseException e) {
            updateError();
        }
    }

    public void updateAddress(Address addr, String address, int cityId, String phone, Timestamp lastUpdate, String lastUpdateBy) {
        try {
            //Server:
            DateFormat df = DateFormat.getInstance();
            if(Main.lmbIdent == LMBIdentifier.PHOENIX) {
                df.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
            } else if(Main.lmbIdent == LMBIdentifier.LONDON) {
                df.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            } else {
                df.setTimeZone(TimeZone.getTimeZone("America/New York"));
            }
            Timestamp newTS = new Timestamp(df.parse(df.format(lastUpdate)).getTime());
            String ins = "UPDATE address SET address='" + address + "', cityId='" + cityId + "', phone='" + phone +
                    "', lastUpdate='" + lastUpdate.toString() + "', lastupdateBy='" + lastUpdateBy +
                    "' WHERE addressId='" + addr.getAddressId() + "'";
            DatabaseConnector.getInstance().request(ins);

            //Local:
            addr.setAddress(address);
            addr.setCityId(cityId);
            addr.setPhoneNumber(phone);
            addr.setLastUpdate(lastUpdate);
            addr.setLastUpdateBy(lastUpdateBy);
        } catch(ParseException e) {
            updateError();
        }
    }

    private void updateError() {
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newError("Erreur de mise à jour", "Il y a eu une erreur lors du traitement de votre" +
                    " demande. Veuillez réessayer plus tard.");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newError("Error de actualización", "Hubo un error al procesar su solicitud. Por " +
                    "favor, inténtelo de nuevo más tarde.");
        } else {
            Main.newError("Update Error", "There was an error processing your request. Please try " +
                    "again later.");
        }
    }

    public Customer newCustomer(String customerName, int addressId, int active, Date createDate, String createdBy,
                                Timestamp lastUpdate, String lastUpdateBy) {
        DateFormat df = DateFormat.getInstance();
        if(Main.lmbIdent == LMBIdentifier.PHOENIX) {
            df.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
        } else if(Main.lmbIdent == LMBIdentifier.LONDON) {
            df.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        } else {
            df.setTimeZone(TimeZone.getTimeZone("America/New York"));
        }
        Customer customer = null;
        try {
            java.util.Date newCreateDate = df.parse(df.format(createDate));
            Date newNewDate = new Date(newCreateDate.getTime());
            customer = new Customer(-1, customerName, addressId, active, newNewDate, createdBy, lastUpdate,
                    lastUpdateBy);
            String ins = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdate, " +
                    "lastUpdateBy) VALUES ('" + customerName + "', '" + addressId + "', '" + active + "', '"
                    + createDate.toString() + "', '" + createdBy + "', '" + lastUpdate.toString() + "', '"
                    + lastUpdateBy + "')";
            DatabaseConnector.getInstance().request(ins);
            ResultSet rs = DatabaseConnector.getInstance().request("SELECT customerId FROM customer WHERE " +
                    "customerName='" + customerName + "' AND addressId='" + addressId + "'");
            rs.next();
            customer.setCustomerId(rs.getInt("customerId"));
            customerMap.put(customer.getCustomerId(), customer);
        } catch(SQLException | ParseException e) {
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newError("newCustomer() Erreur", "Une erreur s'est produite lors de la création " +
                        "d'un nouveau client. Veuillez réessayer.\nMessage d'erreur: " + e.getMessage());
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newError("newCustomer() Error", "Se produjo un error al crear un nuevo cliente." +
                        " Vuelva a intentarlo.\nMensaje de eError: " + e.getMessage());
            } else {
                Main.newError("newCustomer() Error", "Error occurred while creating a new Customer, " +
                        "please try again.\nError Message: " + e.getMessage());
            }
        }
        return customer;
    }

    public void newUser(String userName, String password, int active, Date createDate, Timestamp lastUpdate,
                        String lastUpdateBy) {
        Date newDate = convertToUTCFromLocal(createDate);
        String ins = String.format("INSERT INTO user(userName, password, active, createDate, createdBy, lastUpdate, " +
                "lastUpdateBy) VALUES ('" + userName + "', '" + password + "', '" + active + "', '"
                + newDate.toString() + "', '" + lastUpdate.toString() +"', '" + lastUpdateBy);
        DatabaseConnector.getInstance().request(ins);
        //After this, we're tossing this data, we don't want nor should store anything tbh.. We aren't even passing
        // this data to the Logger
    }

    public Address newAddress(String address, String address2, String cityName, String countryName, String postalCode,
                              String phoneNumber, Date createDate, String createdBy, Timestamp lastUpdate,
                              String lastUpdateBy) {
        Date newDate = convertToUTCFromLocal(createDate);
        City city = null;
        for(Integer i : cityMap.keySet()) {
            if(cityMap.get(i).getCity().equalsIgnoreCase(cityName)) {
                city = cityMap.get(i);
            }
        }
        if(city == null) {
            Country ctry = null;
            for(Integer i : countryMap.keySet()) {
                if(countryName.equalsIgnoreCase(countryMap.get(i).getCountry())) {
                    ctry = countryMap.get(i);
                    break;
                }
            }
            if(ctry == null) {
                ctry = newCountry(countryName, createDate, createdBy, lastUpdate, lastUpdateBy);
                DatabaseConnector.getInstance().request("INSERT INTO country(country, createDate, createdBy, " +
                        "lastUpdate, lastUpdatedBy) VALUES ('" + countryName + "', '"
                        + convertToUTCFromLocal(createDate).toString() + "', '" + createdBy + "', '"
                        + convertToUTCFromLocal(lastUpdate).toString() + "', '" + lastUpdateBy + "')");
                ResultSet rs = DatabaseConnector.getInstance().request("SELECT countryId FROM country WHERE " +
                        "country='" + countryName + "' AND createDate='" + convertToUTCFromLocal(createDate).toString()
                        + "'");
                try {
                    rs.next();
                    ctry.setCountryId(rs.getInt("countryId"));
                    countryMap.put(ctry.getCountryId(), ctry);
                } catch(SQLException e) {
                    if(Main.langIdent == LanguageIdentifier.FRENCH) {
                        Main.newError("Erreur lors de la création du pays", "Une erreur s'est produite" +
                                " lors de la création du pays avec le serveur.");
                    } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                        Main.newError("Error al Crear el País", "Se produjo un error al crear el país " +
                                "con el servidor.");
                    } else {
                        Main.newError("Error Creating Country", "There was an error while creating the" +
                                " country with the server.");
                    }
                }
            }
            city = newCity(cityName, ctry.getCountryId(), createDate, createdBy, lastUpdate, lastUpdateBy);
        }
        Address addr = new Address(-1, address, address2, city.getCityId(), postalCode, phoneNumber, newDate,
                createdBy, lastUpdate, lastUpdateBy);
        String ins = String.format("INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, " +
                "createdBy, lastUpdate, lastUpdateBy) VALUES ('" + addr.getAddress() + "', '" + addr.getAddress2() +
                "', '" + addr.getCityId() + "', '" + addr.getPostalCode() + "', '" + addr.getPhoneNumber() + "', '" +
                addr.getCreateDate().toString() + "', '" + addr.getCreatedBy() + "', '" +
                addr.getLastUpdate().toString() + "', '" + addr.getLastUpdateBy() + "')");
        DatabaseConnector.getInstance().request(ins);
        ResultSet rs = DatabaseConnector.getInstance().request("SELECT * FROM address WHERE address='" + address
                + "' AND cityId=" + addr.getCityId());
        try {
            rs.next();
            addr.setAddressId(rs.getInt("addressId"));
            addressMap.put(rs.getInt("addressId"), addr);
        } catch(SQLException e) {
            //This shouldn't error out, unless there's no internet... shhh we're gonna handle this poorly for now...
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newError("newAddress() Erreur", "Une erreur s'est produite lors de la création " +
                        "d'une nouvelle addresse. Veuillez réessayer.");
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newError("newAddress() Error", "Se produjo un error la crear una nueva dirección." +
                        " Veulve a intentarlo.");
            } else {
                Main.newError("newAddress() Error", "Error occurred while creating a new address, " +
                        "please try again.");
            }
        }
        return addr;
    }

    public City newCity(String cityName, int countryId, Date createDate, String createdBy, Timestamp lastUpdate,
                        String lastUpdateBy) {
        Date newDate = convertToUTCFromLocal(createDate);
        Timestamp newLastUpdate = convertToUTCFromLocal(lastUpdate);
        City city = null;
        for(Integer i : cityMap.keySet()) {
            if(cityMap.get(i).getCity().equalsIgnoreCase(cityName)) {
                city = cityMap.get(i);
            }
        }
        if(city == null) {
            city = new City(-1, cityName, countryId, createDate, createdBy, lastUpdate, lastUpdateBy);
            DatabaseConnector.getInstance().request("INSERT INTO city(city, countryId, createDate, createdBy," +
                    " lastUpdate, lastUpdateBy) VALUES ('" + cityName + "', '" + countryId + "', '" +
                    createDate.toString() + "', '" + createdBy + "', '" + lastUpdate.toString() + "', '" +
                    lastUpdateBy + "')");
            ResultSet rs = DatabaseConnector.getInstance().request("SELECT * FROM city WHERE city='" +
                    cityName + "' AND countryId=" + countryId);
            try {
                rs.next();
                city.setCityId(rs.getInt("cityId"));
                cityMap.put(city.getCityId(), city);
            } catch(SQLException e) {
                if(Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newError("Erreur de Création de Ville", "Erreur lors de la tentative de " +
                            "création d'une nouvelle ville, échec d'obtention de l'identifiant de la ville a partir " +
                            "du serveur.");
                } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newError("Error de Creación de Ciudad", "Error al intentar crear una nueva " +
                            "ciudad, no se pudo obtener la identificación de la ciudad del servidor");
                } else {
                    Main.newError("City Creation Error", "Error trying to create a new city, failed " +
                            "getting City ID from server.");
                }
            }
        }
        return city;
    }

    public Country newCountry(String countryName, Date createDate, String createdBy, Timestamp lastUpdate,
                              String lastUpdateBy) {
        Country ctry = null;
        for(Integer i : countryMap.keySet()) {
            if(countryMap.get(i).getCountry().equalsIgnoreCase(countryName)) {
                ctry = countryMap.get(i);
                break;
            }
        }
        if(ctry == null) {
            DatabaseConnector.getInstance().request("INSERT INTO country(country, createDate, createdBy, " +
                    "lastUpdate, lastUpdateBy) VALUES ('" + countryName + "', '" +
                    convertToUTCFromLocal(createDate).toString() + "', '" + createdBy + "', '" +
                    convertToUTCFromLocal(lastUpdate) + "', '" + lastUpdateBy + "')");
            ResultSet rs = DatabaseConnector.getInstance().request("SELECT countryId FROM country WHERE " +
                    "country='" + countryName + "' AND createDate='" + createDate.toString() + "'");
            try {
                rs.next();
                ctry.setCountryId(rs.getInt("countryId"));
                countryMap.put(ctry.getCountryId(), ctry);
            } catch(SQLException e) {
                if(Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newError("Erreur lors de la création due pays", "Impossible de créer un pays " +
                            "avec un nom " + countryName);
                } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newError("Error al crear el país", "No se puede crear un país con nombre " +
                            countryName);
                } else {
                    Main.newError("Error Creating Country", "I can't find a country called " + countryName
                            + " in serverland.");
                }
            }
        }
        return ctry;
    }

    public Appointment newAppointment(int customerId, int userId, String title, String description, String location,
                                      String contact, String type, String url, Timestamp start, Timestamp end,
                                      Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        Date newDate = convertToUTCFromLocal(createDate);
        Timestamp newStart = convertToUTCFromLocal(start);
        Timestamp newEnd = convertToUTCFromLocal(end);
        Appointment appoint = new Appointment(-1, customerId, userId, title, description, location,
                contact, type, url, start, end, newDate, createdBy, lastUpdate, lastUpdateBy);
        String ins = "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, " +
                "start, end, createDate, createdBy, lastUpdate, lastUpdateBy) " + "VALUES (" + customerId + ", " +
                userId + ", '" + title + "', '" + description + "', '" + location + "', '" + contact + "', '" + type +
                "', '" + url + "', '" + newStart.toString() + "', '" + newEnd.toString() + "', '" + newDate.toString() +
                "', '" + createdBy + "', '" + newDate.toString() + "', '" + lastUpdateBy + "')"; //14
        DatabaseConnector.getInstance().request(ins);
        ResultSet rs = DatabaseConnector.getInstance().request("SELECT appointmentId, title, location, " +
                "contact FROM appointment WHERE title='" + title + "' AND location='" + location + "' AND contact='" +
                contact + "'");
        try {
            rs.next();
            appoint.setAppointmentId(rs.getInt("appointmentId"));
            appointmentMap.put(appoint.getAppointmentId(), appoint);
        } catch(SQLException e) {
            //We're just...gonna repeat line 243..
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newError("newAppointment() Erreur", "Une erreur s'est produite lors de la " +
                        "création d'un nouveau rendez-vous, veuillez réessayer.");
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newError("newAppointment() Error", "Se produjo un error al crear una nueva cita," +
                        " intente neuvamente.");
            } else {
                Main.newError("newAppointment() Error", "Error occurred while creating a new " +
                        "appointment, please try again.");
            }
            e.printStackTrace();
        }
        return appoint;
    }

    public void deleteCustomer(int key) {
        customerMap.remove(key);
        DATABASEINSTANCE.request("DELETE FROM customer WHERE customerId='" + key + "'");
    }

    public void deleteCountry(int key) {
        countryMap.remove(key);
        DATABASEINSTANCE.request("DELETE FROM country WHERE countryId='" + key + "'");
    }

    public void deleteCity(int key) {
        cityMap.remove(key);
        DATABASEINSTANCE.request("DELETE FROM city WHERE cityId='" + key + "'");
    }

    public void deleteAppointment(int key) {
        appointmentMap.remove(key);
        DATABASEINSTANCE.request("DELETE FROM appointment WHERE appointmentId='" + key + "'");
    }

    public void deleteAddress(int key) {
        addressMap.remove(key);
        DATABASEINSTANCE.request("DELETE FROM address WHERE addressId='" + key + "'");
    }
}
