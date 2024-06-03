package com.example.managy.managy.Service;
import java.util.List;

import org.springframework.data.domain.Page;
public interface CrudService<T> {
    T add(T t);
    void delete(T t);
    T update(T t);
    List<T> getList();
    Page<T> findPaginated(int pageNo, int pageSize);
}
