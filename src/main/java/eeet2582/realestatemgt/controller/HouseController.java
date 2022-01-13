package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.service.HouseService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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

import java.util.Collections;
import java.util.List;

import static eeet2582.realestatemgt.service.HouseService.HOUSE_BATCH_SIZE;

/*
NON-AUTHORIZED AND AUTHORIZED USER CAN:
- getFilteredHouses : search for houses in pagination and filters
- getFilteredHousesByPriceBetween : search for houses with price
- getHouseById : see each house in details
*/

/*
ADMIN CAN:
- addNewHouse : add new house into database
- updateHouseById : update an existing house by id
- addHouseImage : add more images into an existing house
- deleteHouseById : delete house by id
 */

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
        List<House> currentBatch = houseService.getFilteredHousesCache(query.trim(), sortBy, orderBy, batchNo);

        return getSubPageFromHouseBatch(pageNo, pageSize, visitedEntries, batchNo, currentBatch);
    }

    // Return houses within a price range
    @GetMapping("/search/byPriceBetween")
    public Page<House> getFilteredHousesByPriceBetween(@RequestParam(value = "low", defaultValue = "100000") Double low,
                                                       @RequestParam(value = "high", defaultValue = "300000") Double high,
                                                       @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                       @RequestParam(value = "sortBy", defaultValue = "price") String sortBy,
                                                       @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {

        // If variables out of range
        if (low >= high || low > 300000 || high < 100000) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(0, pageSize), 0);
        }

        int visitedEntries = Math.max(0, (pageNo - 1) * pageSize);
        int batchNo = visitedEntries / HOUSE_BATCH_SIZE;
        List<House> currentBatch = houseService.getFilteredHousesByPriceBetweenCache(low, high, sortBy, orderBy, batchNo);

        return getSubPageFromHouseBatch(pageNo, pageSize, visitedEntries, batchNo, currentBatch);
    }

    @NotNull
    private Page<House> getSubPageFromHouseBatch(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                 int visitedEntries,
                                                 int batchNo,
                                                 List<House> currentBatch) {
        // "Reset" start after each batch
        int start = visitedEntries - (HOUSE_BATCH_SIZE * batchNo);
        int end = start + pageSize;

        // "Reset" sub-page request (of current batch)
        int subPageNo = pageNo - (HOUSE_BATCH_SIZE / pageSize) * batchNo;
        Pageable pageable = PageRequest.of(subPageNo - 1, pageSize);

        try {
            return new PageImpl<>(currentBatch.subList(start, end), pageable, currentBatch.size());
        } catch (Exception e) {
            // If not enough results to have a full page, shift end
            end = currentBatch.size();

            // If invalid request, return empty page
            if (start > end) {
                return new PageImpl<>(Collections.emptyList(), pageable, currentBatch.size());
            }
            // Else return all is left
            return new PageImpl<>(currentBatch.subList(start, end), pageable, currentBatch.size());
        }
    }

    // Get one by ID
    @GetMapping("/{houseId}")
    @Cacheable(key = "#houseId", value = "House")
    public House getHouseById(@PathVariable("houseId") Long houseId) {
        return houseService.getHouseById(houseId);
    }

    // Add new one (manually put in cache)
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
    @CacheEvict(key = "#houseId", value = "House")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public void updateHouseById(@PathVariable("houseId") Long houseId, @RequestBody House house) {
        houseService.updateHouseById(houseId, house);
    }

    // Add more images to a house by ID
    @PutMapping(
            path = "/addHouseImage",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CacheEvict(key = "#houseId", value = "House")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public void addHouseImage(@RequestParam("houseId") Long houseId, @RequestParam("files") MultipartFile[] file) {
        houseService.addHouseImage(houseId, file);
    }

    // Delete one by ID
    @DeleteMapping("/{houseId}")
    @CacheEvict(key = "#houseId", value = "House")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public ResponseEntity<String> deleteHouseById(@PathVariable("houseId") Long houseId) {
        return new ResponseEntity<>(houseService.deleteHouseById(houseId), HttpStatus.OK);
    }
}
