package com.jungha.chatSide.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlaggedMessageRepository extends JpaRepository<FlaggedMessage, Long> {

}
