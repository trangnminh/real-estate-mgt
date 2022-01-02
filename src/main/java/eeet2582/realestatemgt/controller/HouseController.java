package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static eeet2582.realestatemgt.service.HouseService.HOUSE_BATCH_SIZE;

@RestController
@RequestMapping("api/v1/houses")
@CrossOrigin(origins = "*")
public class HouseController {

    private final HouseService houseService;

    @Autowired
    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    // Return items matching query with sort, order and pagination
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/search")
    public Page<House> getFilteredHouses(@RequestParam(value = "query", defaultValue = "") String query,
                                         @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                                         @RequestParam(value = "orderBy", defaultValue = "asc") String orderBy) {
        int visitedEntries = Math.max(0, (pageNo - 1) * pageSize);
        int batchNo = visitedEntries / HOUSE_BATCH_SIZE;
        List<House> currentBatch = houseService.getFilteredHousesCache(query, sortBy, orderBy, batchNo);

        int start = visitedEntries - (HOUSE_BATCH_SIZE * batchNo);
        int end = start + pageSize;

        int subPageNo = pageNo - (HOUSE_BATCH_SIZE / pageSize) * batchNo;
        Pageable pageable = PageRequest.of(subPageNo - 1, pageSize);
        return new PageImpl<>(currentBatch.subList(start, end), pageable, currentBatch.size());
    }

    // Return houses within a price range
    @GetMapping("/search/byPriceBetween")
    public Page<House> getFilteredHousesByPriceBetween(@RequestParam(value = "low", defaultValue = "0") Double low,
                                                       @RequestParam(value = "high", defaultValue = "900000") Double high,
                                                       @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                       @RequestParam(value = "sortBy", defaultValue = "price") String sortBy,
                                                       @RequestParam(value = "orderBy", defaultValue = "asc") String orderBy) {

        int visitedEntries = Math.max(0, (pageNo - 1) * pageSize);
        int batchNo = visitedEntries / HOUSE_BATCH_SIZE;
        List<House> currentBatch = houseService.getFilteredHousesByPriceBetweenCache(low, high, sortBy, orderBy, batchNo);

        int start = visitedEntries - (HOUSE_BATCH_SIZE * batchNo);
        int end = start + pageSize;

        int subPageNo = pageNo - (HOUSE_BATCH_SIZE / pageSize) * batchNo;
        Pageable pageable = PageRequest.of(subPageNo - 1, pageSize);
        return new PageImpl<>(currentBatch.subList(start, end), pageable, currentBatch.size());
    }

    // Get one by ID
    @GetMapping("/{houseId}")
    @Cacheable(key = "#houseId", value = "House")
    public House getHouseById(@PathVariable("houseId") Long houseId) {
        return houseService.getHouseById(houseId);
    }

    // Add new one
    @PostMapping(
            path = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public ResponseEntity<String> addNewHouse(@ModelAttribute House house, @RequestParam("files") MultipartFile[] file) {
        return new ResponseEntity<>(houseService.addNewHouse(house, file), HttpStatus.OK);
    }

    // Update one by ID
    @PutMapping("/{houseId}")
    @CachePut(key = "#houseId", value = "House")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public ResponseEntity<String> updateHouseById(@PathVariable("houseId") Long houseId, @RequestBody House house) {
        return new ResponseEntity<>(houseService.updateHouseById(houseId, house), HttpStatus.OK);
    }

    // Add more images to a house by ID
    @PutMapping(
            path = "/addHouseImage",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CachePut(key = "#houseId", value = "House")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public ResponseEntity<String> addHouseImage(@RequestParam("houseId") Long houseId, @RequestParam("files") MultipartFile[] file) {
        return new ResponseEntity<>(houseService.addHouseImage(houseId, file), HttpStatus.OK);
    }

    // Delete one by ID
    @DeleteMapping("/{houseId}")
    @CacheEvict(key = "#houseId", value = "House")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public ResponseEntity<String> deleteHouseById(@PathVariable("houseId") Long houseId) {
        return new ResponseEntity<>(houseService.deleteHouseById(houseId), HttpStatus.OK);
    }
}
