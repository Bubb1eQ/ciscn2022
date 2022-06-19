package ctf.dao;

import ctf.model.Service;

public interface ServiceDao {

    void addService(Service service);

    Service getServiceById(String uuid);

    Service getServiceByName(String name);
}
