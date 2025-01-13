package com.myprojects.reminder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sender {
    @Id
    @SequenceGenerator(name = "pk_seq_1",sequenceName ="pk_seq_1",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_seq_1")
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL)
    private List<Notice> notices = new ArrayList<>();

    public void addNotice(Notice notice) {
        notices.add(notice);
    }
}

