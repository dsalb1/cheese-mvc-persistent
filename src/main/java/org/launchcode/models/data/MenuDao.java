package org.launchcode.models.data;


import org.launchcode.models.Menu;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Dan on 6/21/2017.
 */
public interface MenuDao extends CrudRepository<Menu, Integer> {
}
