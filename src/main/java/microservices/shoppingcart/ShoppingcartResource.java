package microservices.shoppingcart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shoppingcart")

public class ShoppingcartResource {

    private static Logger logger = LogManager.getLogger(ShoppingcartResource.class);

    @Autowired
    private ShoppingcartRepository shoppingcartRepository;

    @GetMapping("/all")
    public List<Shoppingcart> getAll() {
        return shoppingcartRepository.findAll();
    }

    @GetMapping("/all/cid/{cid}/caller/{caller}")
    public List<Shoppingcart> getAll(@PathVariable int cid, @PathVariable String caller) {
        logger.info("RES\t{}\t{}\tshoppingcart\t/all/", cid, caller);
        return shoppingcartRepository.findAll();
    }

    @GetMapping("/{id}")
    public Shoppingcart getOne(@PathVariable int id) {
        Optional<Shoppingcart> one = shoppingcartRepository.findById(id);
        return one.get();
    }

    @GetMapping("/{id}/cid/{cid}/caller/{caller}")
    public Shoppingcart getOne(@PathVariable int id, @PathVariable int cid, @PathVariable String caller) {
        logger.info("RES\t{}\t{}\tshoppingcart\t/{}", cid, caller, id);
        Optional<Shoppingcart> one = shoppingcartRepository.findById(id);
        return one.get();
    }

    @PostMapping("/create/{id}/product/{pid}/cid/{cid}/caller/{caller}")
    public void create(@PathVariable int id, @PathVariable int pid, @PathVariable int cid, @PathVariable String caller) {
        logger.info("RES\t{}\t{}\tshoppingcart\t/create/{}/product/{}", cid, caller, id, pid);
        Shoppingcart shoppingcart = new Shoppingcart();
        shoppingcart.setId(id);
        shoppingcart.setProductId(pid);

        double price = 0.0;
        try {
            logger.info("REQ\t{}\tshoppingcart\tproducts\t/price/{}", cid, pid);
            String productUri = "http://localhost:8092/product/price/" + pid + "/cid/" + cid + "/caller/shoppingcart";
            RestTemplate restTemplate = new RestTemplate();
            price = restTemplate.getForObject(productUri, Double.class);
            shoppingcart.setPrice(price);
        }catch(Exception e){
            logger.info("ERR\t{}\tshoppingcart\tproducts\t/price/{}\t{}", cid, pid, e.getMessage());
            }
        Shoppingcart savedShoppingcart = shoppingcartRepository.save(shoppingcart);
    }

    //TODO put
    //TODO delete
}
