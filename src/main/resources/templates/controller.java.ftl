package ${package};

import com.gzcss.starter.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import ${basePackage}.service.${className}Service;
<#if isMainTable>
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzcss.common.api.vo.Result;
import com.gzcss.common.model.PageParam;
import ${basePackage}.model.${className}Model;
import ${basePackage}.model.${className}PageModel;
import ${basePackage}.model.${className}SaveModel;
import ${basePackage}.model.${className}SearchParam;

import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
</#if>

/**
 * ${classComment.comment!''}
 *
 * @author ${classComment.author!''}
 * @date   ${classComment.date!''}
 */
@RestController
@Api(tags = {"${moduleCn!''}"})
@Slf4j
public class ${className}Controller extends BaseController {

    @Autowired
    private ${className}Service ${classNameVar}Service;
<#if isMainTable>
    @ApiOperation(value = "分页查询${moduleCn!''}", tags = {"${moduleCn!''}"})
    @GetMapping("/page")
    public Result<IPage<${className}PageModel>> page${className}(${className}SearchParam searchParam, PageParam pageParam) {
        // 构建分页查询
        IPage<${className}PageModel> pageList = ${classNameVar}Service.pageQuery(searchParam, pageParam);
        return Result.buildSuccess("查询成功", pageList);
    }


    @ApiOperation(value = "保存${moduleCn!''}", tags = {"${moduleCn!''}"})
    @PostMapping("")
    public Result<String> saveOrUpdate(@RequestBody @Validated ${className}SaveModel model) {
        ${classNameVar}Service.saveOrUpdateModel(model);
        return Result.buildSuccess("保存成功", model.getId());
    }

    @ApiOperation(value = "删除${moduleCn!''}", tags = {"${moduleCn!''}"})
    @DeleteMapping("/deleteById")
    public Result<Void> delete(@RequestParam(name = "id") String id) {
        ${classNameVar}Service.removeById(id);
        return Result.buildSuccess("删除成功");
    }

    @ApiOperation(value = "批量删除${moduleCn!''}", tags = {"${moduleCn!''}"})
    @DeleteMapping("/deleteBatch")
    public Result<Void> deleteBatch(@RequestBody List<String> ids) {
        ${classNameVar}Service.removeByIds(ids);
        return Result.buildSuccess("删除成功");
    }

    @ApiOperation(value = "通过id查询${moduleCn!''}", tags = {"${moduleCn!''}"})
    @GetMapping("/getById")
    public Result<${className}Model> getModelById(@RequestParam(name = "id") String id) {
        ${className}Model model = ${classNameVar}Service.getModelById(id);
        return Result.buildSuccess("查询成功", model);
    }
</#if>
}
