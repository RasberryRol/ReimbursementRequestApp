package org.example;

import java.util.Collection;

public interface Repository<E, ID>{

    E getById(ID id);

    Collection<User> getAll(Integer accountId);

    Integer updateBalance(User balance, int ID);

    ID save(E item);
}