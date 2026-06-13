package com.projeto_pida.locadora.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service

public class FileStorageService {

    private final Path uploadDir = Paths.get("uploads");

    public FileStorageService() {
        // Cria o diretório se não existir
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de uploads", e);
        }
    }

    public String salvarImagem(MultipartFile file) {
        try {
            String nomeOriginal = file.getOriginalFilename();
            String extensao = "";
            if (nomeOriginal != null && nomeOriginal.contains(".")) {
                extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            }
            String nomeArquivo = UUID.randomUUID().toString() + extensao;
            Path caminhoDestino = uploadDir.resolve(nomeArquivo);
            Files.copy(file.getInputStream(), caminhoDestino, StandardCopyOption.REPLACE_EXISTING);

            // Retorna a URL pública (ajuste a base conforme sua configuração)
            return "http://localhost:8081/uploads/" + nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem", e);
        }
    }
}