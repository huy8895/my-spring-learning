package com.example.postgresdemo.dao;

import com.example.postgresdemo.dto.PostDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Repository
public class PostDAO {
    @PersistenceContext
    EntityManager entityManager;

    public List<PostDto> getPosts() {
        List<PostDto> postDTOs = entityManager.createQuery("" +
                "select new com.example.postgresdemo.dto.PostDto(\n" +
                "                   p.id,\n" +
                "                   p.title\n" +
                "                )\n" +
                "                from Post p\n" +
//                "                where p.createOn < :fromTimestamp" +
                "", PostDto.class)
//                                              .setParameter(
//                                                      "fromTimestamp",
//                                                      Timestamp.from(
//                                                              LocalDate.of(2020, 1, 1)
//                                                                       .atStartOfDay()
//                                                                       .toInstant(ZoneOffset.UTC)
//                                                      )
//                                              )
                                              .getResultList();

        return postDTOs;
    }

    public List<Tuple> getPostByTuple(){
        List<Tuple> postDTOs = entityManager.createNativeQuery(""+
                "    SELECT\n" +
                "       p.id AS id,\n" +
                "       p.title AS title\n" +
                "    FROM Post p\n" +
                "", Tuple.class)
                                            .getResultList();


        Tuple postDTO = postDTOs.get(0);

        return postDTOs;
    }
}
