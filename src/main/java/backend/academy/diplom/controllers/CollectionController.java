//package backend.academy.diplom.controllers;
//
//import backend.academy.diplom.DTO.collection.CollectionDTO;
//import backend.academy.diplom.DTO.collection.EditCollectionDTO;
//import backend.academy.diplom.services.CollectionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/collection")
//public class CollectionController {
//    private final CollectionService collectionService;
//
//    @GetMapping("/{name}")
//    public CollectionDTO getCollection(@PathVariable String name,
//                                       @RequestHeader("Authorization") String authHeader) {
//        return collectionService.getCollection(authHeader, name);
//    }
//
//    @PostMapping
//    public void addCollection(@RequestBody CollectionDTO collectionDTO,
//                              @RequestHeader("Authorization") String authHeader) {
//        collectionService.addCollection(authHeader, collectionDTO.name(),
//                collectionDTO.description());
//    }
//
//    @DeleteMapping("/{name}")
//    public void deleteCollection(@PathVariable String name,
//                                 @RequestHeader("Authorization") String authHeader) {
//        collectionService.deleteCollection(authHeader, name);
//    }
//
//    @PutMapping
//    public void editCollection(@RequestBody EditCollectionDTO editCollection,
//                              @RequestHeader("Authorization") String authHeader) {
//        collectionService.editCollection(authHeader, editCollection.oldName(),
//                editCollection.newName(), editCollection.description());
//    }
//}
//
//
//
